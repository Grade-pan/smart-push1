package com.sed.exceptionhandle;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;

import com.sed.content.ExceptionNotice;
import com.sed.content.HttpExceptionNotice;
import com.sed.message.INoticeSendComponent;
import com.sed.pojos.ExceptionStatistics;
import com.sed.properties.ExceptionNoticeFrequencyStrategy;
import com.sed.properties.ExceptionNoticeProperty;
import com.sed.redis.ExceptionRedisStorageComponent;
/**核心处理类,信息发送前的校验
* */
public class ExceptionHandler {
	/**redis存储组件
	* */
	private ExceptionRedisStorageComponent exceptionRedisStorageComponent;
	/**核心配置文件参数信息对象
	 * */
	private ExceptionNoticeProperty exceptionNoticeProperty;
	/**异常信息通知策略配置文件：主要分为两个，时间策略与次数策略
	 * */
	private ExceptionNoticeFrequencyStrategy exceptionNoticeFrequencyStrategy;
	/**用来存储异常信息，String 表示的是负责人，INoticeSendComponent消息类型组件
	 * 责任人与具体的发送组件对应，例如：sed : dingding ,sed : email
	 * */
	private final Map<String, INoticeSendComponent> blameMap = new HashMap<>();
	/**用安全Map来存储m每一个异常信息的ExceptionStatistics通知策略对象，key是唯一ID,
	 * ExceptionStatistics类里面有一个唯一标识uid
	 * */
	private final Map<String, ExceptionStatistics> checkUid = Collections.synchronizedMap(new HashMap<>());

	private final Log logger = LogFactory.getLog(getClass());

	public ExceptionHandler(ExceptionNoticeProperty exceptionNoticeProperty,
			ExceptionNoticeFrequencyStrategy exceptionNoticeFrequencyStrategy) {
		this.exceptionNoticeFrequencyStrategy = exceptionNoticeFrequencyStrategy;
		this.exceptionNoticeProperty = exceptionNoticeProperty;
	}

	public ExceptionNoticeProperty getExceptionNoticeProperty() {
		return exceptionNoticeProperty;
	}

	public void setExceptionNoticeProperty(ExceptionNoticeProperty exceptionNoticeProperty) {
		this.exceptionNoticeProperty = exceptionNoticeProperty;
	}

	public ExceptionNoticeFrequencyStrategy getExceptionNoticeFrequencyStrategy() {
		return exceptionNoticeFrequencyStrategy;
	}

	public void setExceptionNoticeFrequencyStrategy(ExceptionNoticeFrequencyStrategy exceptionNoticeFrequencyStrategy) {
		this.exceptionNoticeFrequencyStrategy = exceptionNoticeFrequencyStrategy;
	}

	public ExceptionRedisStorageComponent getExceptionRedisStorageComponent() {
		return exceptionRedisStorageComponent;
	}

	public Map<String, INoticeSendComponent> getBlameMap() {
		return blameMap;
	}

	/**
	 * @param exceptionRedisStorageComponent the exceptionRedisStorageComponent to
	 *                                       set
	 */
	public void setExceptionRedisStorageComponent(ExceptionRedisStorageComponent exceptionRedisStorageComponent) {
		this.exceptionRedisStorageComponent = exceptionRedisStorageComponent;
	}
	/**注册消息发送组件
	 * 首先获取INoticeSendComponent里的map的key集合，该map集合里面存储了负责人——负责人具体信息,此时key集合就是所有负责人信息。
	 * 然后开始遍历负责人信息 并存在本类下的blameMap。
	* */
	public void registerNoticeSendComponent(INoticeSendComponent component) {
		component.getAllBuddies().forEach(x -> blameMap.putIfAbsent(x, component));
	}

	/**
	 * 最基础的异常通知的创建方法
	 * 第一步：获取具体负责人
	 * 第二部：判断该异常是否是要排除异常
	 * 第三步：创建ExceptionNotice
	 * 第四步：判断exceptionNotice是否可以发送，会根据配置策略等信息来判断
	 * @param blamedFor 谁背锅？
	 * @param exception 异常信息
	 * 
	 * @return
	 */
	public ExceptionNotice createNotice(String blamedFor, RuntimeException exception) {
		blamedFor = checkBlameFor(blamedFor);
		if (containsException(exception))
			return null;
		ExceptionNotice exceptionNotice = new ExceptionNotice(exception,
				exceptionNoticeProperty.getIncludedTracePackage(), null);
		exceptionNotice.setProject(exceptionNoticeProperty.getProjectName());
		boolean noHas = persist(exceptionNotice);
		if (noHas)
			messageSend(blamedFor, exceptionNotice);
		return exceptionNotice;

	}
	/**
	 * 判断异常信息是否存在
	 * 第一步：获取传入异常的所有clazz信息
	 * 第二步：用异常拒绝策略返回配置文件所配置的排除异常类
	 * 第三步：判断如果传入异常里面有一个已排除异常类就返回true
	 *
	* */
	private boolean containsException(RuntimeException exception) {
		List<Class<? extends Throwable>> thisEClass = getAllExceptionClazz(exception);
		List<Class<? extends RuntimeException>> list = exceptionNoticeProperty.getExcludeExceptions();
		for (Class<? extends RuntimeException> clazz : list) {
			if (thisEClass.stream().anyMatch(c -> clazz.isAssignableFrom(c)))
				return true;
		}
		return false;
	}
	/**
	 * 获取该异常信息下面的所有异常，并且一次遍历存入到List
	* */
	private List<Class<? extends Throwable>> getAllExceptionClazz(RuntimeException exception) {
		List<Class<? extends Throwable>> list = new LinkedList<Class<? extends Throwable>>();
		list.add(exception.getClass());
		Throwable cause = exception.getCause();
		while (cause != null) {
			list.add(cause.getClass());
			cause = cause.getCause();
		}
		return list;
	}

	/**
	 * 反射方式获取方法中出现的异常进行的通知
	 * 第一步：获取具体负责人
	 * 第二部：判断该异常是否是要排除异常
	 * 第三步：创建ExceptionNotice
	 * 第四步：判断exceptionNotice是否可以发送，会根据配置策略等信息来判断
	 * 第五步：发送信息
	 * @param blamedFor 具体负责人
	 * @param ex        异常信息
	 * @param method    方法名
	 * @param args      参数信息
	 * @return
	 */
	public ExceptionNotice createNotice(String blamedFor, RuntimeException ex, String method, Object[] args) {
		blamedFor = checkBlameFor(blamedFor);
		if (containsException(ex))
			return null;
		ExceptionNotice exceptionNotice = new ExceptionNotice(ex, exceptionNoticeProperty.getIncludedTracePackage(),
				args);
		exceptionNotice.setProject(exceptionNoticeProperty.getProjectName());
		boolean noHas = persist(exceptionNotice);
		if (noHas)
			messageSend(blamedFor, exceptionNotice);
		return exceptionNotice;

	}

	/**
	 * 创建一个http请求异常的通知
	 * 第一步：获取具体负责人
	 * 第二部：判断该异常是否是要排除异常
	 * 第三步：创建ExceptionNotice
	 * 第四步：判断exceptionNotice是否可以发送，会根据配置策略等信息来判断
	 * 第五步：发送信息
	 * @param blamedFor
	 * @param exception
	 * @param url
	 * @param param
	 * @param requesBody
	 * @param headers
	 * @return
	 */
	public HttpExceptionNotice createHttpNotice(String blamedFor, RuntimeException exception, String url,
			Map<String, String> param, String requesBody, Map<String, String> headers) {
		blamedFor = checkBlameFor(blamedFor);
		if (containsException(exception))
			return null;
		HttpExceptionNotice exceptionNotice = new HttpExceptionNotice(exception,
				exceptionNoticeProperty.getIncludedTracePackage(), url, param, requesBody, headers);
		exceptionNotice.setProject(exceptionNoticeProperty.getProjectName());
		boolean noHas = persist(exceptionNotice);
		if (noHas)
			messageSend(blamedFor, exceptionNotice);
		return exceptionNotice;
	}
	/**
	 * 判断该ExceptionNotice策略里面的内容是否已存在
	 * 第一步：获取uid,并在该类里面的map里面获得uid所对应的ExceptionStatistics通知策略
	* */

	/**可优化的方法，先判断是否开启异常处理策略，再开始执行是其他操作
	* */
	private boolean persist(ExceptionNotice exceptionNotice) {
		Boolean needNotice = false;
		String uid = exceptionNotice.getUid();
		ExceptionStatistics exceptionStatistics = checkUid.get(uid);
		logger.debug(exceptionStatistics);
		if (exceptionStatistics != null) {
			//先将该异常的通知策略加一
			Long count = exceptionStatistics.plusOne();
			//判断异常处理策略是否开启
			if (exceptionNoticeFrequencyStrategy.getEnabled()) {
				//判断根据配置策略来检测异常信息是否发送
				if (stratergyCheck(exceptionStatistics, exceptionNoticeFrequencyStrategy)) {
					LocalDateTime now = LocalDateTime.now();
					exceptionNotice.setLatestShowTime(now);
					exceptionNotice.setShowCount(count);
					exceptionStatistics.setLastShowedCount(count);
					exceptionStatistics.setNoticeTime(now);
					needNotice = true;
				}
			}
		} else {
			//ExceptionStatistics策略对象不存在,安全存入checkUid
			exceptionStatistics = new ExceptionStatistics(uid);
			synchronized (exceptionStatistics) {
				checkUid.put(uid, exceptionStatistics);
				needNotice = true;
			}
		}
		//并且可以将本通知存入redis,以hash的类型存入
		if (exceptionRedisStorageComponent != null)
			exceptionRedisStorageComponent.save(exceptionNotice);
		return needNotice;
	}
	/**
	 * 来判断该责任人具体是谁，如果确定不了，就采用默人责任人
	* */
	private String checkBlameFor(String blameFor) {
		blameFor = StringUtils.isEmpty(blameFor) || (!blameMap.containsKey(blameFor))
				? exceptionNoticeProperty.getDefaultNotice()
				: blameFor;
		return blameFor;
	}

	/**
	* 判断异常处理策略
	 * 如果是TIMEOUT，需要判断是否超时
	 * 如果是SHOWCOUNT，判断是否超出规定次数
	* */
	private boolean stratergyCheck(ExceptionStatistics exceptionStatistics,
			ExceptionNoticeFrequencyStrategy exceptionNoticeFrequencyStrategy) {
		switch (exceptionNoticeFrequencyStrategy.getFrequencyType()) {
		case TIMEOUT:
			Duration dur = Duration.between(exceptionStatistics.getNoticeTime(), LocalDateTime.now());
			return exceptionNoticeFrequencyStrategy.getNoticeTimeInterval().compareTo(dur) < 0;
		//如果showcount超出阀数，则一直通知
		case SHOWCOUNT:
			return exceptionStatistics.getShowCount().longValue() - exceptionStatistics.getLastShowedCount()
					.longValue() > exceptionNoticeFrequencyStrategy.getNoticeShowCount().longValue();
		}
		return false;
	}
	/**
	 * 发送信息，需要责任人，消息异常对象
	* */
	private void messageSend(String blamedFor, ExceptionNotice exceptionNotice) {
		//通过责任人获取发送信息的组件，这个组件的具体实现类来完成发送信息
		INoticeSendComponent sendComponent = blameMap.get(blamedFor);
		sendComponent.send(blamedFor, exceptionNotice);
	}
	/**时间归置，超出25分钟就清除所缓存的每个异常消息的异常策略
	* */
	@Scheduled(cron = "0 25 0 * * * ")
	public void resetCheck() {
		checkUid.clear();
	}
}
