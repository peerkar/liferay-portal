<#include "copyright.txt" parse="false">

package ${apiPackagePath}.exception;

import com.liferay.portal.kernel.exception.PortalException;

/**
 * @author ${author}
 */
public class ${exception}Exception extends PortalException {

	public ${exception}Exception() {
	}

	public ${exception}Exception(String msg) {
		super(msg);
	}

	public ${exception}Exception(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public ${exception}Exception(Throwable throwable) {
		super(throwable);
	}

}