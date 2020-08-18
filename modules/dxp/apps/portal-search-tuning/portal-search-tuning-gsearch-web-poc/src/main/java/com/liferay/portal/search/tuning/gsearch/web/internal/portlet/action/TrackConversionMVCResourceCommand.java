
package com.liferay.portal.search.tuning.gsearch.web.internal.portlet.action;

import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.search.tuning.gsearch.web.internal.constants.GSearchWebPortletKeys;
import com.liferay.portal.search.tuning.gsearch.web.internal.constants.ResourceRequestKeys;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;

/**
 * @author Petteri Karttunen
 */
@Component(
	immediate = true, 
	property = {
		"javax.portlet.name=" + GSearchWebPortletKeys.GSEARCH_WEB,
		"mvc.command.name=" + ResourceRequestKeys.TRACK_CONVERSION
	}, 
	service = MVCResourceCommand.class
)
public class TrackConversionMVCResourceCommand extends BaseMVCResourceCommand {

	@Override
	protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws Exception {

		//track(resourceRequest);
	}
/*
	@SuppressWarnings({ "unchecked" })
	protected String getLastKeywords(ResourceRequest resourceRequest) {

		PortletSession session = resourceRequest.getPortletSession();

		return (String)session.getAttribute(
					SessionAttributes.PREVIOUS_SEARCH_PHRASE, 
					PortletSession.APPLICATION_SCOPE);
	}

	protected void track(ResourceRequest resourceRequest) {

		if (_conversionTrackingLocalService == null) {
			_log.error("Conversion tracking service is not installed or failed to activate.");
			return;
		}
				
		try {
			
			long entryClassPK = ParamUtil.getLong(resourceRequest, "trackId");
			String lastKeywords = getLastKeywords(resourceRequest);

			ServiceContext serviceContext = ServiceContextFactory.getInstance(
					Clicks.class.getName(), resourceRequest);

			serviceContext.setAttribute("keywords", lastKeywords);
			serviceContext.setAttribute("entryClassPK", entryClassPK);
			
			_conversionTrackingLocalService.updateClicks(serviceContext);
			
		} catch (PortalException e) {
			_log.error(e.getMessage(), e);
		}
	}

	
	
	private static final Log _log =
					LogFactoryUtil.getLog(TrackConversionMVCResourceCommand.class);

	private volatile GSearchWebConfiguration _moduleConfiguration;
	
	@Reference(
		cardinality=ReferenceCardinality.OPTIONAL
	)
	private volatile ConversionTrackingLocalService _conversionTrackingLocalService;
	
	@Reference(
			target = ModuleServiceLifecycle.PORTAL_INITIALIZED
	)
	ModuleServiceLifecycle _portalInitialized;
*/
}
