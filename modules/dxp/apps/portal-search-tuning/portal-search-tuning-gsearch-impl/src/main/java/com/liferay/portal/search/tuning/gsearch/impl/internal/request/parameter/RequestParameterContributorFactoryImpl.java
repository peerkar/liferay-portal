package com.liferay.portal.search.tuning.gsearch.impl.internal.request.parameter;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.search.tuning.gsearch.impl.internal.component.ServiceComponentReference;
import com.liferay.portal.search.tuning.gsearch.impl.internal.request.parameter.contributor.RequestParameterContributor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * @author Petteri Karttunen
 */
@Component(immediate = true, service = RequestParameterContributorFactory.class)
public class RequestParameterContributorFactoryImpl
	implements RequestParameterContributorFactory {

	@Override
	public RequestParameterContributor getContributor(String type)
		throws IllegalArgumentException {

		ServiceComponentReference<RequestParameterContributor>
			serviceComponentReference = _requestParameterContributors.get(type);

		if (serviceComponentReference == null) {
			throw new IllegalArgumentException(
				"No registered request parameter contributor " + type);
		}

		return serviceComponentReference.getServiceComponent();
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC
	)
	protected void registerRequestParameterContributor(
		RequestParameterContributor parameterContributor,
		Map<String, Object> properties) {

		String type = (String)properties.get("type");

		if (Validator.isBlank(type)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to add request parameter contributor " +
						parameterContributor.getClass(
						).getName() + ". Type property empty.");
			}

			return;
		}

		int serviceRanking = GetterUtil.get(
			properties.get("service.ranking"), 0);

		ServiceComponentReference<RequestParameterContributor>
			serviceComponentReference = new ServiceComponentReference<>(
				parameterContributor, serviceRanking);

		if (_requestParameterContributors.containsKey(type)) {
			ServiceComponentReference<RequestParameterContributor>
				previousReference = _requestParameterContributors.get(type);

			if (previousReference.compareTo(serviceComponentReference) < 0) {
				_requestParameterContributors.put(
					type, serviceComponentReference);
			}
		} else {
			_requestParameterContributors.put(type, serviceComponentReference);
		}
	}
	
	protected void unregisterRequestParameterContributor(
			RequestParameterContributor parameterContributor,
			Map<String, Object> properties) {

		String type = (String)properties.get("type");

		if (Validator.isBlank(type)) {
			return;
		}

		_requestParameterContributors.remove(type);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		RequestParameterContributorFactoryImpl.class);

	private volatile Map
		<String, ServiceComponentReference<RequestParameterContributor>>
			_requestParameterContributors = new ConcurrentHashMap<>();

}