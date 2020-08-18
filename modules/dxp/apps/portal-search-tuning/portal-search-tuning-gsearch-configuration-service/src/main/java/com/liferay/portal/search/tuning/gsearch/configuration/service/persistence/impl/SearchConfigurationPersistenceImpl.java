/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.portal.search.tuning.gsearch.configuration.service.persistence.impl;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.sanitizer.Sanitizer;
import com.liferay.portal.kernel.sanitizer.SanitizerException;
import com.liferay.portal.kernel.sanitizer.SanitizerUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.security.permission.InlineSQLHelperUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;
import com.liferay.portal.search.tuning.gsearch.configuration.exception.NoSuchConfigurationException;
import com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfiguration;
import com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfigurationTable;
import com.liferay.portal.search.tuning.gsearch.configuration.model.impl.SearchConfigurationImpl;
import com.liferay.portal.search.tuning.gsearch.configuration.model.impl.SearchConfigurationModelImpl;
import com.liferay.portal.search.tuning.gsearch.configuration.service.persistence.SearchConfigurationPersistence;
import com.liferay.portal.search.tuning.gsearch.configuration.service.persistence.impl.constants.SearchPersistenceConstants;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The persistence implementation for the search configuration service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = SearchConfigurationPersistence.class)
public class SearchConfigurationPersistenceImpl
	extends BasePersistenceImpl<SearchConfiguration>
	implements SearchConfigurationPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>SearchConfigurationUtil</code> to access the search configuration persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		SearchConfigurationImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByUuid;
	private FinderPath _finderPathWithoutPaginationFindByUuid;
	private FinderPath _finderPathCountByUuid;

	/**
	 * Returns all the search configurations where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByUuid(String uuid) {
		return findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the search configurations where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByUuid(
		String uuid, int start, int end) {

		return findByUuid(uuid, start, end, null);
	}

	/**
	 * Returns an ordered range of all the search configurations where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return findByUuid(uuid, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the search configurations where uuid = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByUuid(
		String uuid, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator,
		boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByUuid;
				finderArgs = new Object[] {uuid};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByUuid;
			finderArgs = new Object[] {uuid, start, end, orderByComparator};
		}

		List<SearchConfiguration> list = null;

		if (useFinderCache) {
			list = (List<SearchConfiguration>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (SearchConfiguration searchConfiguration : list) {
					if (!uuid.equals(searchConfiguration.getUuid())) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_SEARCHCONFIGURATION_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(SearchConfigurationModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				list = (List<SearchConfiguration>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first search configuration in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration findByUuid_First(
			String uuid,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		SearchConfiguration searchConfiguration = fetchByUuid_First(
			uuid, orderByComparator);

		if (searchConfiguration != null) {
			return searchConfiguration;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchConfigurationException(sb.toString());
	}

	/**
	 * Returns the first search configuration in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration fetchByUuid_First(
		String uuid, OrderByComparator<SearchConfiguration> orderByComparator) {

		List<SearchConfiguration> list = findByUuid(
			uuid, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last search configuration in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration findByUuid_Last(
			String uuid,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		SearchConfiguration searchConfiguration = fetchByUuid_Last(
			uuid, orderByComparator);

		if (searchConfiguration != null) {
			return searchConfiguration;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append("}");

		throw new NoSuchConfigurationException(sb.toString());
	}

	/**
	 * Returns the last search configuration in the ordered set where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration fetchByUuid_Last(
		String uuid, OrderByComparator<SearchConfiguration> orderByComparator) {

		int count = countByUuid(uuid);

		if (count == 0) {
			return null;
		}

		List<SearchConfiguration> list = findByUuid(
			uuid, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set where uuid = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param uuid the uuid
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	@Override
	public SearchConfiguration[] findByUuid_PrevAndNext(
			long searchConfigurationId, String uuid,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		uuid = Objects.toString(uuid, "");

		SearchConfiguration searchConfiguration = findByPrimaryKey(
			searchConfigurationId);

		Session session = null;

		try {
			session = openSession();

			SearchConfiguration[] array = new SearchConfigurationImpl[3];

			array[0] = getByUuid_PrevAndNext(
				session, searchConfiguration, uuid, orderByComparator, true);

			array[1] = searchConfiguration;

			array[2] = getByUuid_PrevAndNext(
				session, searchConfiguration, uuid, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected SearchConfiguration getByUuid_PrevAndNext(
		Session session, SearchConfiguration searchConfiguration, String uuid,
		OrderByComparator<SearchConfiguration> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_SEARCHCONFIGURATION_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_UUID_3);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_UUID_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(SearchConfigurationModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindUuid) {
			queryPos.add(uuid);
		}

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						searchConfiguration)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<SearchConfiguration> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the search configurations where uuid = &#63; from the database.
	 *
	 * @param uuid the uuid
	 */
	@Override
	public void removeByUuid(String uuid) {
		for (SearchConfiguration searchConfiguration :
				findByUuid(uuid, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(searchConfiguration);
		}
	}

	/**
	 * Returns the number of search configurations where uuid = &#63;.
	 *
	 * @param uuid the uuid
	 * @return the number of matching search configurations
	 */
	@Override
	public int countByUuid(String uuid) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid;

		Object[] finderArgs = new Object[] {uuid};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_SEARCHCONFIGURATION_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_UUID_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_UUID_UUID_2 =
		"searchConfiguration.uuid = ?";

	private static final String _FINDER_COLUMN_UUID_UUID_3 =
		"(searchConfiguration.uuid IS NULL OR searchConfiguration.uuid = '')";

	private FinderPath _finderPathFetchByUUID_G;
	private FinderPath _finderPathCountByUUID_G;

	/**
	 * Returns the search configuration where uuid = &#63; and groupId = &#63; or throws a <code>NoSuchConfigurationException</code> if it could not be found.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration findByUUID_G(String uuid, long groupId)
		throws NoSuchConfigurationException {

		SearchConfiguration searchConfiguration = fetchByUUID_G(uuid, groupId);

		if (searchConfiguration == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("uuid=");
			sb.append(uuid);

			sb.append(", groupId=");
			sb.append(groupId);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchConfigurationException(sb.toString());
		}

		return searchConfiguration;
	}

	/**
	 * Returns the search configuration where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration fetchByUUID_G(String uuid, long groupId) {
		return fetchByUUID_G(uuid, groupId, true);
	}

	/**
	 * Returns the search configuration where uuid = &#63; and groupId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration fetchByUUID_G(
		String uuid, long groupId, boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {uuid, groupId};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(
				_finderPathFetchByUUID_G, finderArgs, this);
		}

		if (result instanceof SearchConfiguration) {
			SearchConfiguration searchConfiguration =
				(SearchConfiguration)result;

			if (!Objects.equals(uuid, searchConfiguration.getUuid()) ||
				(groupId != searchConfiguration.getGroupId())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_SEARCHCONFIGURATION_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_G_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_G_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_G_GROUPID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(groupId);

				List<SearchConfiguration> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByUUID_G, finderArgs, list);
					}
				}
				else {
					SearchConfiguration searchConfiguration = list.get(0);

					result = searchConfiguration;

					cacheResult(searchConfiguration);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(
						_finderPathFetchByUUID_G, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (SearchConfiguration)result;
		}
	}

	/**
	 * Removes the search configuration where uuid = &#63; and groupId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the search configuration that was removed
	 */
	@Override
	public SearchConfiguration removeByUUID_G(String uuid, long groupId)
		throws NoSuchConfigurationException {

		SearchConfiguration searchConfiguration = findByUUID_G(uuid, groupId);

		return remove(searchConfiguration);
	}

	/**
	 * Returns the number of search configurations where uuid = &#63; and groupId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param groupId the group ID
	 * @return the number of matching search configurations
	 */
	@Override
	public int countByUUID_G(String uuid, long groupId) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUUID_G;

		Object[] finderArgs = new Object[] {uuid, groupId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_SEARCHCONFIGURATION_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_G_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_G_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_G_GROUPID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(groupId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_UUID_G_UUID_2 =
		"searchConfiguration.uuid = ? AND ";

	private static final String _FINDER_COLUMN_UUID_G_UUID_3 =
		"(searchConfiguration.uuid IS NULL OR searchConfiguration.uuid = '') AND ";

	private static final String _FINDER_COLUMN_UUID_G_GROUPID_2 =
		"searchConfiguration.groupId = ?";

	private FinderPath _finderPathWithPaginationFindByUuid_C;
	private FinderPath _finderPathWithoutPaginationFindByUuid_C;
	private FinderPath _finderPathCountByUuid_C;

	/**
	 * Returns all the search configurations where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByUuid_C(String uuid, long companyId) {
		return findByUuid_C(
			uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the search configurations where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByUuid_C(
		String uuid, long companyId, int start, int end) {

		return findByUuid_C(uuid, companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the search configurations where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return findByUuid_C(
			uuid, companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the search configurations where uuid = &#63; and companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByUuid_C(
		String uuid, long companyId, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator,
		boolean useFinderCache) {

		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByUuid_C;
				finderArgs = new Object[] {uuid, companyId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByUuid_C;
			finderArgs = new Object[] {
				uuid, companyId, start, end, orderByComparator
			};
		}

		List<SearchConfiguration> list = null;

		if (useFinderCache) {
			list = (List<SearchConfiguration>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (SearchConfiguration searchConfiguration : list) {
					if (!uuid.equals(searchConfiguration.getUuid()) ||
						(companyId != searchConfiguration.getCompanyId())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					4 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(4);
			}

			sb.append(_SQL_SELECT_SEARCHCONFIGURATION_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(SearchConfigurationModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(companyId);

				list = (List<SearchConfiguration>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first search configuration in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration findByUuid_C_First(
			String uuid, long companyId,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		SearchConfiguration searchConfiguration = fetchByUuid_C_First(
			uuid, companyId, orderByComparator);

		if (searchConfiguration != null) {
			return searchConfiguration;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchConfigurationException(sb.toString());
	}

	/**
	 * Returns the first search configuration in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration fetchByUuid_C_First(
		String uuid, long companyId,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		List<SearchConfiguration> list = findByUuid_C(
			uuid, companyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last search configuration in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration findByUuid_C_Last(
			String uuid, long companyId,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		SearchConfiguration searchConfiguration = fetchByUuid_C_Last(
			uuid, companyId, orderByComparator);

		if (searchConfiguration != null) {
			return searchConfiguration;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("uuid=");
		sb.append(uuid);

		sb.append(", companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchConfigurationException(sb.toString());
	}

	/**
	 * Returns the last search configuration in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration fetchByUuid_C_Last(
		String uuid, long companyId,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		int count = countByUuid_C(uuid, companyId);

		if (count == 0) {
			return null;
		}

		List<SearchConfiguration> list = findByUuid_C(
			uuid, companyId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set where uuid = &#63; and companyId = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	@Override
	public SearchConfiguration[] findByUuid_C_PrevAndNext(
			long searchConfigurationId, String uuid, long companyId,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		uuid = Objects.toString(uuid, "");

		SearchConfiguration searchConfiguration = findByPrimaryKey(
			searchConfigurationId);

		Session session = null;

		try {
			session = openSession();

			SearchConfiguration[] array = new SearchConfigurationImpl[3];

			array[0] = getByUuid_C_PrevAndNext(
				session, searchConfiguration, uuid, companyId,
				orderByComparator, true);

			array[1] = searchConfiguration;

			array[2] = getByUuid_C_PrevAndNext(
				session, searchConfiguration, uuid, companyId,
				orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected SearchConfiguration getByUuid_C_PrevAndNext(
		Session session, SearchConfiguration searchConfiguration, String uuid,
		long companyId,
		OrderByComparator<SearchConfiguration> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_SEARCHCONFIGURATION_WHERE);

		boolean bindUuid = false;

		if (uuid.isEmpty()) {
			sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
		}
		else {
			bindUuid = true;

			sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
		}

		sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(SearchConfigurationModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		if (bindUuid) {
			queryPos.add(uuid);
		}

		queryPos.add(companyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						searchConfiguration)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<SearchConfiguration> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the search configurations where uuid = &#63; and companyId = &#63; from the database.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 */
	@Override
	public void removeByUuid_C(String uuid, long companyId) {
		for (SearchConfiguration searchConfiguration :
				findByUuid_C(
					uuid, companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(searchConfiguration);
		}
	}

	/**
	 * Returns the number of search configurations where uuid = &#63; and companyId = &#63;.
	 *
	 * @param uuid the uuid
	 * @param companyId the company ID
	 * @return the number of matching search configurations
	 */
	@Override
	public int countByUuid_C(String uuid, long companyId) {
		uuid = Objects.toString(uuid, "");

		FinderPath finderPath = _finderPathCountByUuid_C;

		Object[] finderArgs = new Object[] {uuid, companyId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_SEARCHCONFIGURATION_WHERE);

			boolean bindUuid = false;

			if (uuid.isEmpty()) {
				sb.append(_FINDER_COLUMN_UUID_C_UUID_3);
			}
			else {
				bindUuid = true;

				sb.append(_FINDER_COLUMN_UUID_C_UUID_2);
			}

			sb.append(_FINDER_COLUMN_UUID_C_COMPANYID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindUuid) {
					queryPos.add(uuid);
				}

				queryPos.add(companyId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_UUID_C_UUID_2 =
		"searchConfiguration.uuid = ? AND ";

	private static final String _FINDER_COLUMN_UUID_C_UUID_3 =
		"(searchConfiguration.uuid IS NULL OR searchConfiguration.uuid = '') AND ";

	private static final String _FINDER_COLUMN_UUID_C_COMPANYID_2 =
		"searchConfiguration.companyId = ?";

	private FinderPath _finderPathWithPaginationFindByGroupId;
	private FinderPath _finderPathWithoutPaginationFindByGroupId;
	private FinderPath _finderPathCountByGroupId;

	/**
	 * Returns all the search configurations where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByGroupId(long groupId) {
		return findByGroupId(
			groupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the search configurations where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByGroupId(
		long groupId, int start, int end) {

		return findByGroupId(groupId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the search configurations where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByGroupId(
		long groupId, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return findByGroupId(groupId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the search configurations where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByGroupId(
		long groupId, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByGroupId;
				finderArgs = new Object[] {groupId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByGroupId;
			finderArgs = new Object[] {groupId, start, end, orderByComparator};
		}

		List<SearchConfiguration> list = null;

		if (useFinderCache) {
			list = (List<SearchConfiguration>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (SearchConfiguration searchConfiguration : list) {
					if (groupId != searchConfiguration.getGroupId()) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_SEARCHCONFIGURATION_WHERE);

			sb.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(SearchConfigurationModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				list = (List<SearchConfiguration>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first search configuration in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration findByGroupId_First(
			long groupId,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		SearchConfiguration searchConfiguration = fetchByGroupId_First(
			groupId, orderByComparator);

		if (searchConfiguration != null) {
			return searchConfiguration;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append("}");

		throw new NoSuchConfigurationException(sb.toString());
	}

	/**
	 * Returns the first search configuration in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration fetchByGroupId_First(
		long groupId,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		List<SearchConfiguration> list = findByGroupId(
			groupId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last search configuration in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration findByGroupId_Last(
			long groupId,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		SearchConfiguration searchConfiguration = fetchByGroupId_Last(
			groupId, orderByComparator);

		if (searchConfiguration != null) {
			return searchConfiguration;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append("}");

		throw new NoSuchConfigurationException(sb.toString());
	}

	/**
	 * Returns the last search configuration in the ordered set where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration fetchByGroupId_Last(
		long groupId,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		int count = countByGroupId(groupId);

		if (count == 0) {
			return null;
		}

		List<SearchConfiguration> list = findByGroupId(
			groupId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set where groupId = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	@Override
	public SearchConfiguration[] findByGroupId_PrevAndNext(
			long searchConfigurationId, long groupId,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		SearchConfiguration searchConfiguration = findByPrimaryKey(
			searchConfigurationId);

		Session session = null;

		try {
			session = openSession();

			SearchConfiguration[] array = new SearchConfigurationImpl[3];

			array[0] = getByGroupId_PrevAndNext(
				session, searchConfiguration, groupId, orderByComparator, true);

			array[1] = searchConfiguration;

			array[2] = getByGroupId_PrevAndNext(
				session, searchConfiguration, groupId, orderByComparator,
				false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected SearchConfiguration getByGroupId_PrevAndNext(
		Session session, SearchConfiguration searchConfiguration, long groupId,
		OrderByComparator<SearchConfiguration> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_SEARCHCONFIGURATION_WHERE);

		sb.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(SearchConfigurationModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(groupId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						searchConfiguration)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<SearchConfiguration> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the search configurations that the user has permission to view where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the matching search configurations that the user has permission to view
	 */
	@Override
	public List<SearchConfiguration> filterFindByGroupId(long groupId) {
		return filterFindByGroupId(
			groupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the search configurations that the user has permission to view where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of matching search configurations that the user has permission to view
	 */
	@Override
	public List<SearchConfiguration> filterFindByGroupId(
		long groupId, int start, int end) {

		return filterFindByGroupId(groupId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the search configurations that the user has permissions to view where groupId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching search configurations that the user has permission to view
	 */
	@Override
	public List<SearchConfiguration> filterFindByGroupId(
		long groupId, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		if (!InlineSQLHelperUtil.isEnabled(groupId)) {
			return findByGroupId(groupId, start, end, orderByComparator);
		}

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				3 + (orderByComparator.getOrderByFields().length * 2));
		}
		else {
			sb = new StringBundler(4);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_SEARCHCONFIGURATION_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_SEARCHCONFIGURATION_NO_INLINE_DISTINCT_WHERE_1);
		}

		sb.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_SEARCHCONFIGURATION_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			if (getDB().isSupportsInlineDistinct()) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator, true);
			}
			else {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_TABLE, orderByComparator, true);
			}
		}
		else {
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(SearchConfigurationModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(SearchConfigurationModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), SearchConfiguration.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupId);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			if (getDB().isSupportsInlineDistinct()) {
				sqlQuery.addEntity(
					_FILTER_ENTITY_ALIAS, SearchConfigurationImpl.class);
			}
			else {
				sqlQuery.addEntity(
					_FILTER_ENTITY_TABLE, SearchConfigurationImpl.class);
			}

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(groupId);

			return (List<SearchConfiguration>)QueryUtil.list(
				sqlQuery, getDialect(), start, end);
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set of search configurations that the user has permission to view where groupId = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param groupId the group ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	@Override
	public SearchConfiguration[] filterFindByGroupId_PrevAndNext(
			long searchConfigurationId, long groupId,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		if (!InlineSQLHelperUtil.isEnabled(groupId)) {
			return findByGroupId_PrevAndNext(
				searchConfigurationId, groupId, orderByComparator);
		}

		SearchConfiguration searchConfiguration = findByPrimaryKey(
			searchConfigurationId);

		Session session = null;

		try {
			session = openSession();

			SearchConfiguration[] array = new SearchConfigurationImpl[3];

			array[0] = filterGetByGroupId_PrevAndNext(
				session, searchConfiguration, groupId, orderByComparator, true);

			array[1] = searchConfiguration;

			array[2] = filterGetByGroupId_PrevAndNext(
				session, searchConfiguration, groupId, orderByComparator,
				false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected SearchConfiguration filterGetByGroupId_PrevAndNext(
		Session session, SearchConfiguration searchConfiguration, long groupId,
		OrderByComparator<SearchConfiguration> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_SEARCHCONFIGURATION_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_SEARCHCONFIGURATION_NO_INLINE_DISTINCT_WHERE_1);
		}

		sb.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_SEARCHCONFIGURATION_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				if (getDB().isSupportsInlineDistinct()) {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_ALIAS, orderByConditionFields[i],
							true));
				}
				else {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_TABLE, orderByConditionFields[i],
							true));
				}

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				if (getDB().isSupportsInlineDistinct()) {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_ALIAS, orderByFields[i], true));
				}
				else {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_TABLE, orderByFields[i], true));
				}

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(SearchConfigurationModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(SearchConfigurationModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), SearchConfiguration.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupId);

		SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

		sqlQuery.setFirstResult(0);
		sqlQuery.setMaxResults(2);

		if (getDB().isSupportsInlineDistinct()) {
			sqlQuery.addEntity(
				_FILTER_ENTITY_ALIAS, SearchConfigurationImpl.class);
		}
		else {
			sqlQuery.addEntity(
				_FILTER_ENTITY_TABLE, SearchConfigurationImpl.class);
		}

		QueryPos queryPos = QueryPos.getInstance(sqlQuery);

		queryPos.add(groupId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						searchConfiguration)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<SearchConfiguration> list = sqlQuery.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the search configurations where groupId = &#63; from the database.
	 *
	 * @param groupId the group ID
	 */
	@Override
	public void removeByGroupId(long groupId) {
		for (SearchConfiguration searchConfiguration :
				findByGroupId(
					groupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(searchConfiguration);
		}
	}

	/**
	 * Returns the number of search configurations where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the number of matching search configurations
	 */
	@Override
	public int countByGroupId(long groupId) {
		FinderPath finderPath = _finderPathCountByGroupId;

		Object[] finderArgs = new Object[] {groupId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_SEARCHCONFIGURATION_WHERE);

			sb.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of search configurations that the user has permission to view where groupId = &#63;.
	 *
	 * @param groupId the group ID
	 * @return the number of matching search configurations that the user has permission to view
	 */
	@Override
	public int filterCountByGroupId(long groupId) {
		if (!InlineSQLHelperUtil.isEnabled(groupId)) {
			return countByGroupId(groupId);
		}

		StringBundler sb = new StringBundler(2);

		sb.append(_FILTER_SQL_COUNT_SEARCHCONFIGURATION_WHERE);

		sb.append(_FINDER_COLUMN_GROUPID_GROUPID_2);

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), SearchConfiguration.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupId);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			sqlQuery.addScalar(
				COUNT_COLUMN_NAME, com.liferay.portal.kernel.dao.orm.Type.LONG);

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(groupId);

			Long count = (Long)sqlQuery.uniqueResult();

			return count.intValue();
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	private static final String _FINDER_COLUMN_GROUPID_GROUPID_2 =
		"searchConfiguration.groupId = ?";

	private FinderPath _finderPathWithPaginationFindByCompanyId;
	private FinderPath _finderPathWithoutPaginationFindByCompanyId;
	private FinderPath _finderPathCountByCompanyId;

	/**
	 * Returns all the search configurations where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByCompanyId(long companyId) {
		return findByCompanyId(
			companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the search configurations where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByCompanyId(
		long companyId, int start, int end) {

		return findByCompanyId(companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the search configurations where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return findByCompanyId(companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the search configurations where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByCompanyId;
				finderArgs = new Object[] {companyId};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByCompanyId;
			finderArgs = new Object[] {
				companyId, start, end, orderByComparator
			};
		}

		List<SearchConfiguration> list = null;

		if (useFinderCache) {
			list = (List<SearchConfiguration>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (SearchConfiguration searchConfiguration : list) {
					if (companyId != searchConfiguration.getCompanyId()) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_SEARCHCONFIGURATION_WHERE);

			sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(SearchConfigurationModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				list = (List<SearchConfiguration>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first search configuration in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration findByCompanyId_First(
			long companyId,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		SearchConfiguration searchConfiguration = fetchByCompanyId_First(
			companyId, orderByComparator);

		if (searchConfiguration != null) {
			return searchConfiguration;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchConfigurationException(sb.toString());
	}

	/**
	 * Returns the first search configuration in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration fetchByCompanyId_First(
		long companyId,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		List<SearchConfiguration> list = findByCompanyId(
			companyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last search configuration in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration findByCompanyId_Last(
			long companyId,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		SearchConfiguration searchConfiguration = fetchByCompanyId_Last(
			companyId, orderByComparator);

		if (searchConfiguration != null) {
			return searchConfiguration;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchConfigurationException(sb.toString());
	}

	/**
	 * Returns the last search configuration in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration fetchByCompanyId_Last(
		long companyId,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		int count = countByCompanyId(companyId);

		if (count == 0) {
			return null;
		}

		List<SearchConfiguration> list = findByCompanyId(
			companyId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set where companyId = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	@Override
	public SearchConfiguration[] findByCompanyId_PrevAndNext(
			long searchConfigurationId, long companyId,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		SearchConfiguration searchConfiguration = findByPrimaryKey(
			searchConfigurationId);

		Session session = null;

		try {
			session = openSession();

			SearchConfiguration[] array = new SearchConfigurationImpl[3];

			array[0] = getByCompanyId_PrevAndNext(
				session, searchConfiguration, companyId, orderByComparator,
				true);

			array[1] = searchConfiguration;

			array[2] = getByCompanyId_PrevAndNext(
				session, searchConfiguration, companyId, orderByComparator,
				false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected SearchConfiguration getByCompanyId_PrevAndNext(
		Session session, SearchConfiguration searchConfiguration,
		long companyId,
		OrderByComparator<SearchConfiguration> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_SEARCHCONFIGURATION_WHERE);

		sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(SearchConfigurationModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(companyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						searchConfiguration)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<SearchConfiguration> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the search configurations where companyId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 */
	@Override
	public void removeByCompanyId(long companyId) {
		for (SearchConfiguration searchConfiguration :
				findByCompanyId(
					companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(searchConfiguration);
		}
	}

	/**
	 * Returns the number of search configurations where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching search configurations
	 */
	@Override
	public int countByCompanyId(long companyId) {
		FinderPath finderPath = _finderPathCountByCompanyId;

		Object[] finderArgs = new Object[] {companyId};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_SEARCHCONFIGURATION_WHERE);

			sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_COMPANYID_COMPANYID_2 =
		"searchConfiguration.companyId = ?";

	private FinderPath _finderPathWithPaginationFindByG_S;
	private FinderPath _finderPathWithoutPaginationFindByG_S;
	private FinderPath _finderPathCountByG_S;

	/**
	 * Returns all the search configurations where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @return the matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByG_S(long groupId, int status) {
		return findByG_S(
			groupId, status, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the search configurations where groupId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByG_S(
		long groupId, int status, int start, int end) {

		return findByG_S(groupId, status, start, end, null);
	}

	/**
	 * Returns an ordered range of all the search configurations where groupId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByG_S(
		long groupId, int status, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return findByG_S(groupId, status, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the search configurations where groupId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByG_S(
		long groupId, int status, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByG_S;
				finderArgs = new Object[] {groupId, status};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByG_S;
			finderArgs = new Object[] {
				groupId, status, start, end, orderByComparator
			};
		}

		List<SearchConfiguration> list = null;

		if (useFinderCache) {
			list = (List<SearchConfiguration>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (SearchConfiguration searchConfiguration : list) {
					if ((groupId != searchConfiguration.getGroupId()) ||
						(status != searchConfiguration.getStatus())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					4 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(4);
			}

			sb.append(_SQL_SELECT_SEARCHCONFIGURATION_WHERE);

			sb.append(_FINDER_COLUMN_G_S_GROUPID_2);

			sb.append(_FINDER_COLUMN_G_S_STATUS_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(SearchConfigurationModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(status);

				list = (List<SearchConfiguration>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first search configuration in the ordered set where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration findByG_S_First(
			long groupId, int status,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		SearchConfiguration searchConfiguration = fetchByG_S_First(
			groupId, status, orderByComparator);

		if (searchConfiguration != null) {
			return searchConfiguration;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", status=");
		sb.append(status);

		sb.append("}");

		throw new NoSuchConfigurationException(sb.toString());
	}

	/**
	 * Returns the first search configuration in the ordered set where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration fetchByG_S_First(
		long groupId, int status,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		List<SearchConfiguration> list = findByG_S(
			groupId, status, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last search configuration in the ordered set where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration findByG_S_Last(
			long groupId, int status,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		SearchConfiguration searchConfiguration = fetchByG_S_Last(
			groupId, status, orderByComparator);

		if (searchConfiguration != null) {
			return searchConfiguration;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", status=");
		sb.append(status);

		sb.append("}");

		throw new NoSuchConfigurationException(sb.toString());
	}

	/**
	 * Returns the last search configuration in the ordered set where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration fetchByG_S_Last(
		long groupId, int status,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		int count = countByG_S(groupId, status);

		if (count == 0) {
			return null;
		}

		List<SearchConfiguration> list = findByG_S(
			groupId, status, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set where groupId = &#63; and status = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param groupId the group ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	@Override
	public SearchConfiguration[] findByG_S_PrevAndNext(
			long searchConfigurationId, long groupId, int status,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		SearchConfiguration searchConfiguration = findByPrimaryKey(
			searchConfigurationId);

		Session session = null;

		try {
			session = openSession();

			SearchConfiguration[] array = new SearchConfigurationImpl[3];

			array[0] = getByG_S_PrevAndNext(
				session, searchConfiguration, groupId, status,
				orderByComparator, true);

			array[1] = searchConfiguration;

			array[2] = getByG_S_PrevAndNext(
				session, searchConfiguration, groupId, status,
				orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected SearchConfiguration getByG_S_PrevAndNext(
		Session session, SearchConfiguration searchConfiguration, long groupId,
		int status, OrderByComparator<SearchConfiguration> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_SEARCHCONFIGURATION_WHERE);

		sb.append(_FINDER_COLUMN_G_S_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_S_STATUS_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(SearchConfigurationModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(groupId);

		queryPos.add(status);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						searchConfiguration)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<SearchConfiguration> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the search configurations that the user has permission to view where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @return the matching search configurations that the user has permission to view
	 */
	@Override
	public List<SearchConfiguration> filterFindByG_S(long groupId, int status) {
		return filterFindByG_S(
			groupId, status, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the search configurations that the user has permission to view where groupId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of matching search configurations that the user has permission to view
	 */
	@Override
	public List<SearchConfiguration> filterFindByG_S(
		long groupId, int status, int start, int end) {

		return filterFindByG_S(groupId, status, start, end, null);
	}

	/**
	 * Returns an ordered range of all the search configurations that the user has permissions to view where groupId = &#63; and status = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching search configurations that the user has permission to view
	 */
	@Override
	public List<SearchConfiguration> filterFindByG_S(
		long groupId, int status, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		if (!InlineSQLHelperUtil.isEnabled(groupId)) {
			return findByG_S(groupId, status, start, end, orderByComparator);
		}

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByFields().length * 2));
		}
		else {
			sb = new StringBundler(5);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_SEARCHCONFIGURATION_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_SEARCHCONFIGURATION_NO_INLINE_DISTINCT_WHERE_1);
		}

		sb.append(_FINDER_COLUMN_G_S_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_S_STATUS_2);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_SEARCHCONFIGURATION_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			if (getDB().isSupportsInlineDistinct()) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator, true);
			}
			else {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_TABLE, orderByComparator, true);
			}
		}
		else {
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(SearchConfigurationModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(SearchConfigurationModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), SearchConfiguration.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupId);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			if (getDB().isSupportsInlineDistinct()) {
				sqlQuery.addEntity(
					_FILTER_ENTITY_ALIAS, SearchConfigurationImpl.class);
			}
			else {
				sqlQuery.addEntity(
					_FILTER_ENTITY_TABLE, SearchConfigurationImpl.class);
			}

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(groupId);

			queryPos.add(status);

			return (List<SearchConfiguration>)QueryUtil.list(
				sqlQuery, getDialect(), start, end);
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set of search configurations that the user has permission to view where groupId = &#63; and status = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param groupId the group ID
	 * @param status the status
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	@Override
	public SearchConfiguration[] filterFindByG_S_PrevAndNext(
			long searchConfigurationId, long groupId, int status,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		if (!InlineSQLHelperUtil.isEnabled(groupId)) {
			return findByG_S_PrevAndNext(
				searchConfigurationId, groupId, status, orderByComparator);
		}

		SearchConfiguration searchConfiguration = findByPrimaryKey(
			searchConfigurationId);

		Session session = null;

		try {
			session = openSession();

			SearchConfiguration[] array = new SearchConfigurationImpl[3];

			array[0] = filterGetByG_S_PrevAndNext(
				session, searchConfiguration, groupId, status,
				orderByComparator, true);

			array[1] = searchConfiguration;

			array[2] = filterGetByG_S_PrevAndNext(
				session, searchConfiguration, groupId, status,
				orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected SearchConfiguration filterGetByG_S_PrevAndNext(
		Session session, SearchConfiguration searchConfiguration, long groupId,
		int status, OrderByComparator<SearchConfiguration> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				6 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(5);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_SEARCHCONFIGURATION_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_SEARCHCONFIGURATION_NO_INLINE_DISTINCT_WHERE_1);
		}

		sb.append(_FINDER_COLUMN_G_S_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_S_STATUS_2);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_SEARCHCONFIGURATION_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				if (getDB().isSupportsInlineDistinct()) {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_ALIAS, orderByConditionFields[i],
							true));
				}
				else {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_TABLE, orderByConditionFields[i],
							true));
				}

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				if (getDB().isSupportsInlineDistinct()) {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_ALIAS, orderByFields[i], true));
				}
				else {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_TABLE, orderByFields[i], true));
				}

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(SearchConfigurationModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(SearchConfigurationModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), SearchConfiguration.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupId);

		SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

		sqlQuery.setFirstResult(0);
		sqlQuery.setMaxResults(2);

		if (getDB().isSupportsInlineDistinct()) {
			sqlQuery.addEntity(
				_FILTER_ENTITY_ALIAS, SearchConfigurationImpl.class);
		}
		else {
			sqlQuery.addEntity(
				_FILTER_ENTITY_TABLE, SearchConfigurationImpl.class);
		}

		QueryPos queryPos = QueryPos.getInstance(sqlQuery);

		queryPos.add(groupId);

		queryPos.add(status);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						searchConfiguration)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<SearchConfiguration> list = sqlQuery.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the search configurations where groupId = &#63; and status = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 */
	@Override
	public void removeByG_S(long groupId, int status) {
		for (SearchConfiguration searchConfiguration :
				findByG_S(
					groupId, status, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(searchConfiguration);
		}
	}

	/**
	 * Returns the number of search configurations where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @return the number of matching search configurations
	 */
	@Override
	public int countByG_S(long groupId, int status) {
		FinderPath finderPath = _finderPathCountByG_S;

		Object[] finderArgs = new Object[] {groupId, status};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_SEARCHCONFIGURATION_WHERE);

			sb.append(_FINDER_COLUMN_G_S_GROUPID_2);

			sb.append(_FINDER_COLUMN_G_S_STATUS_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(status);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of search configurations that the user has permission to view where groupId = &#63; and status = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @return the number of matching search configurations that the user has permission to view
	 */
	@Override
	public int filterCountByG_S(long groupId, int status) {
		if (!InlineSQLHelperUtil.isEnabled(groupId)) {
			return countByG_S(groupId, status);
		}

		StringBundler sb = new StringBundler(3);

		sb.append(_FILTER_SQL_COUNT_SEARCHCONFIGURATION_WHERE);

		sb.append(_FINDER_COLUMN_G_S_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_S_STATUS_2);

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), SearchConfiguration.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupId);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			sqlQuery.addScalar(
				COUNT_COLUMN_NAME, com.liferay.portal.kernel.dao.orm.Type.LONG);

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(groupId);

			queryPos.add(status);

			Long count = (Long)sqlQuery.uniqueResult();

			return count.intValue();
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	private static final String _FINDER_COLUMN_G_S_GROUPID_2 =
		"searchConfiguration.groupId = ? AND ";

	private static final String _FINDER_COLUMN_G_S_STATUS_2 =
		"searchConfiguration.status = ?";

	private FinderPath _finderPathWithPaginationFindByG_T;
	private FinderPath _finderPathWithoutPaginationFindByG_T;
	private FinderPath _finderPathCountByG_T;

	/**
	 * Returns all the search configurations where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @return the matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByG_T(long groupId, int type) {
		return findByG_T(
			groupId, type, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the search configurations where groupId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByG_T(
		long groupId, int type, int start, int end) {

		return findByG_T(groupId, type, start, end, null);
	}

	/**
	 * Returns an ordered range of all the search configurations where groupId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByG_T(
		long groupId, int type, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return findByG_T(groupId, type, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the search configurations where groupId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByG_T(
		long groupId, int type, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByG_T;
				finderArgs = new Object[] {groupId, type};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByG_T;
			finderArgs = new Object[] {
				groupId, type, start, end, orderByComparator
			};
		}

		List<SearchConfiguration> list = null;

		if (useFinderCache) {
			list = (List<SearchConfiguration>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (SearchConfiguration searchConfiguration : list) {
					if ((groupId != searchConfiguration.getGroupId()) ||
						(type != searchConfiguration.getType())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					4 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(4);
			}

			sb.append(_SQL_SELECT_SEARCHCONFIGURATION_WHERE);

			sb.append(_FINDER_COLUMN_G_T_GROUPID_2);

			sb.append(_FINDER_COLUMN_G_T_TYPE_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(SearchConfigurationModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(type);

				list = (List<SearchConfiguration>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first search configuration in the ordered set where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration findByG_T_First(
			long groupId, int type,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		SearchConfiguration searchConfiguration = fetchByG_T_First(
			groupId, type, orderByComparator);

		if (searchConfiguration != null) {
			return searchConfiguration;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", type=");
		sb.append(type);

		sb.append("}");

		throw new NoSuchConfigurationException(sb.toString());
	}

	/**
	 * Returns the first search configuration in the ordered set where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration fetchByG_T_First(
		long groupId, int type,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		List<SearchConfiguration> list = findByG_T(
			groupId, type, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last search configuration in the ordered set where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration findByG_T_Last(
			long groupId, int type,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		SearchConfiguration searchConfiguration = fetchByG_T_Last(
			groupId, type, orderByComparator);

		if (searchConfiguration != null) {
			return searchConfiguration;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", type=");
		sb.append(type);

		sb.append("}");

		throw new NoSuchConfigurationException(sb.toString());
	}

	/**
	 * Returns the last search configuration in the ordered set where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration fetchByG_T_Last(
		long groupId, int type,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		int count = countByG_T(groupId, type);

		if (count == 0) {
			return null;
		}

		List<SearchConfiguration> list = findByG_T(
			groupId, type, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set where groupId = &#63; and type = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param groupId the group ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	@Override
	public SearchConfiguration[] findByG_T_PrevAndNext(
			long searchConfigurationId, long groupId, int type,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		SearchConfiguration searchConfiguration = findByPrimaryKey(
			searchConfigurationId);

		Session session = null;

		try {
			session = openSession();

			SearchConfiguration[] array = new SearchConfigurationImpl[3];

			array[0] = getByG_T_PrevAndNext(
				session, searchConfiguration, groupId, type, orderByComparator,
				true);

			array[1] = searchConfiguration;

			array[2] = getByG_T_PrevAndNext(
				session, searchConfiguration, groupId, type, orderByComparator,
				false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected SearchConfiguration getByG_T_PrevAndNext(
		Session session, SearchConfiguration searchConfiguration, long groupId,
		int type, OrderByComparator<SearchConfiguration> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_SEARCHCONFIGURATION_WHERE);

		sb.append(_FINDER_COLUMN_G_T_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_T_TYPE_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(SearchConfigurationModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(groupId);

		queryPos.add(type);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						searchConfiguration)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<SearchConfiguration> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the search configurations that the user has permission to view where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @return the matching search configurations that the user has permission to view
	 */
	@Override
	public List<SearchConfiguration> filterFindByG_T(long groupId, int type) {
		return filterFindByG_T(
			groupId, type, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the search configurations that the user has permission to view where groupId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of matching search configurations that the user has permission to view
	 */
	@Override
	public List<SearchConfiguration> filterFindByG_T(
		long groupId, int type, int start, int end) {

		return filterFindByG_T(groupId, type, start, end, null);
	}

	/**
	 * Returns an ordered range of all the search configurations that the user has permissions to view where groupId = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching search configurations that the user has permission to view
	 */
	@Override
	public List<SearchConfiguration> filterFindByG_T(
		long groupId, int type, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		if (!InlineSQLHelperUtil.isEnabled(groupId)) {
			return findByG_T(groupId, type, start, end, orderByComparator);
		}

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByFields().length * 2));
		}
		else {
			sb = new StringBundler(5);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_SEARCHCONFIGURATION_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_SEARCHCONFIGURATION_NO_INLINE_DISTINCT_WHERE_1);
		}

		sb.append(_FINDER_COLUMN_G_T_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_T_TYPE_2_SQL);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_SEARCHCONFIGURATION_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			if (getDB().isSupportsInlineDistinct()) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator, true);
			}
			else {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_TABLE, orderByComparator, true);
			}
		}
		else {
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(SearchConfigurationModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(SearchConfigurationModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), SearchConfiguration.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupId);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			if (getDB().isSupportsInlineDistinct()) {
				sqlQuery.addEntity(
					_FILTER_ENTITY_ALIAS, SearchConfigurationImpl.class);
			}
			else {
				sqlQuery.addEntity(
					_FILTER_ENTITY_TABLE, SearchConfigurationImpl.class);
			}

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(groupId);

			queryPos.add(type);

			return (List<SearchConfiguration>)QueryUtil.list(
				sqlQuery, getDialect(), start, end);
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set of search configurations that the user has permission to view where groupId = &#63; and type = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param groupId the group ID
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	@Override
	public SearchConfiguration[] filterFindByG_T_PrevAndNext(
			long searchConfigurationId, long groupId, int type,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		if (!InlineSQLHelperUtil.isEnabled(groupId)) {
			return findByG_T_PrevAndNext(
				searchConfigurationId, groupId, type, orderByComparator);
		}

		SearchConfiguration searchConfiguration = findByPrimaryKey(
			searchConfigurationId);

		Session session = null;

		try {
			session = openSession();

			SearchConfiguration[] array = new SearchConfigurationImpl[3];

			array[0] = filterGetByG_T_PrevAndNext(
				session, searchConfiguration, groupId, type, orderByComparator,
				true);

			array[1] = searchConfiguration;

			array[2] = filterGetByG_T_PrevAndNext(
				session, searchConfiguration, groupId, type, orderByComparator,
				false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected SearchConfiguration filterGetByG_T_PrevAndNext(
		Session session, SearchConfiguration searchConfiguration, long groupId,
		int type, OrderByComparator<SearchConfiguration> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				6 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(5);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_SEARCHCONFIGURATION_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_SEARCHCONFIGURATION_NO_INLINE_DISTINCT_WHERE_1);
		}

		sb.append(_FINDER_COLUMN_G_T_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_T_TYPE_2_SQL);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_SEARCHCONFIGURATION_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				if (getDB().isSupportsInlineDistinct()) {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_ALIAS, orderByConditionFields[i],
							true));
				}
				else {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_TABLE, orderByConditionFields[i],
							true));
				}

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				if (getDB().isSupportsInlineDistinct()) {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_ALIAS, orderByFields[i], true));
				}
				else {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_TABLE, orderByFields[i], true));
				}

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(SearchConfigurationModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(SearchConfigurationModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), SearchConfiguration.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupId);

		SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

		sqlQuery.setFirstResult(0);
		sqlQuery.setMaxResults(2);

		if (getDB().isSupportsInlineDistinct()) {
			sqlQuery.addEntity(
				_FILTER_ENTITY_ALIAS, SearchConfigurationImpl.class);
		}
		else {
			sqlQuery.addEntity(
				_FILTER_ENTITY_TABLE, SearchConfigurationImpl.class);
		}

		QueryPos queryPos = QueryPos.getInstance(sqlQuery);

		queryPos.add(groupId);

		queryPos.add(type);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						searchConfiguration)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<SearchConfiguration> list = sqlQuery.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the search configurations where groupId = &#63; and type = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 */
	@Override
	public void removeByG_T(long groupId, int type) {
		for (SearchConfiguration searchConfiguration :
				findByG_T(
					groupId, type, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(searchConfiguration);
		}
	}

	/**
	 * Returns the number of search configurations where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @return the number of matching search configurations
	 */
	@Override
	public int countByG_T(long groupId, int type) {
		FinderPath finderPath = _finderPathCountByG_T;

		Object[] finderArgs = new Object[] {groupId, type};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_SEARCHCONFIGURATION_WHERE);

			sb.append(_FINDER_COLUMN_G_T_GROUPID_2);

			sb.append(_FINDER_COLUMN_G_T_TYPE_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(type);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of search configurations that the user has permission to view where groupId = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param type the type
	 * @return the number of matching search configurations that the user has permission to view
	 */
	@Override
	public int filterCountByG_T(long groupId, int type) {
		if (!InlineSQLHelperUtil.isEnabled(groupId)) {
			return countByG_T(groupId, type);
		}

		StringBundler sb = new StringBundler(3);

		sb.append(_FILTER_SQL_COUNT_SEARCHCONFIGURATION_WHERE);

		sb.append(_FINDER_COLUMN_G_T_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_T_TYPE_2_SQL);

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), SearchConfiguration.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupId);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			sqlQuery.addScalar(
				COUNT_COLUMN_NAME, com.liferay.portal.kernel.dao.orm.Type.LONG);

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(groupId);

			queryPos.add(type);

			Long count = (Long)sqlQuery.uniqueResult();

			return count.intValue();
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	private static final String _FINDER_COLUMN_G_T_GROUPID_2 =
		"searchConfiguration.groupId = ? AND ";

	private static final String _FINDER_COLUMN_G_T_TYPE_2 =
		"searchConfiguration.type = ?";

	private static final String _FINDER_COLUMN_G_T_TYPE_2_SQL =
		"searchConfiguration.type_ = ?";

	private FinderPath _finderPathWithPaginationFindByG_S_T;
	private FinderPath _finderPathWithoutPaginationFindByG_S_T;
	private FinderPath _finderPathCountByG_S_T;

	/**
	 * Returns all the search configurations where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @return the matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByG_S_T(
		long groupId, int status, int type) {

		return findByG_S_T(
			groupId, status, type, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the search configurations where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByG_S_T(
		long groupId, int status, int type, int start, int end) {

		return findByG_S_T(groupId, status, type, start, end, null);
	}

	/**
	 * Returns an ordered range of all the search configurations where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByG_S_T(
		long groupId, int status, int type, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return findByG_S_T(
			groupId, status, type, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the search configurations where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching search configurations
	 */
	@Override
	public List<SearchConfiguration> findByG_S_T(
		long groupId, int status, int type, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindByG_S_T;
				finderArgs = new Object[] {groupId, status, type};
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindByG_S_T;
			finderArgs = new Object[] {
				groupId, status, type, start, end, orderByComparator
			};
		}

		List<SearchConfiguration> list = null;

		if (useFinderCache) {
			list = (List<SearchConfiguration>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (SearchConfiguration searchConfiguration : list) {
					if ((groupId != searchConfiguration.getGroupId()) ||
						(status != searchConfiguration.getStatus()) ||
						(type != searchConfiguration.getType())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					5 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(5);
			}

			sb.append(_SQL_SELECT_SEARCHCONFIGURATION_WHERE);

			sb.append(_FINDER_COLUMN_G_S_T_GROUPID_2);

			sb.append(_FINDER_COLUMN_G_S_T_STATUS_2);

			sb.append(_FINDER_COLUMN_G_S_T_TYPE_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(SearchConfigurationModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(status);

				queryPos.add(type);

				list = (List<SearchConfiguration>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first search configuration in the ordered set where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration findByG_S_T_First(
			long groupId, int status, int type,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		SearchConfiguration searchConfiguration = fetchByG_S_T_First(
			groupId, status, type, orderByComparator);

		if (searchConfiguration != null) {
			return searchConfiguration;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", status=");
		sb.append(status);

		sb.append(", type=");
		sb.append(type);

		sb.append("}");

		throw new NoSuchConfigurationException(sb.toString());
	}

	/**
	 * Returns the first search configuration in the ordered set where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration fetchByG_S_T_First(
		long groupId, int status, int type,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		List<SearchConfiguration> list = findByG_S_T(
			groupId, status, type, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last search configuration in the ordered set where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration
	 * @throws NoSuchConfigurationException if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration findByG_S_T_Last(
			long groupId, int status, int type,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		SearchConfiguration searchConfiguration = fetchByG_S_T_Last(
			groupId, status, type, orderByComparator);

		if (searchConfiguration != null) {
			return searchConfiguration;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("groupId=");
		sb.append(groupId);

		sb.append(", status=");
		sb.append(status);

		sb.append(", type=");
		sb.append(type);

		sb.append("}");

		throw new NoSuchConfigurationException(sb.toString());
	}

	/**
	 * Returns the last search configuration in the ordered set where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching search configuration, or <code>null</code> if a matching search configuration could not be found
	 */
	@Override
	public SearchConfiguration fetchByG_S_T_Last(
		long groupId, int status, int type,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		int count = countByG_S_T(groupId, status, type);

		if (count == 0) {
			return null;
		}

		List<SearchConfiguration> list = findByG_S_T(
			groupId, status, type, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	@Override
	public SearchConfiguration[] findByG_S_T_PrevAndNext(
			long searchConfigurationId, long groupId, int status, int type,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		SearchConfiguration searchConfiguration = findByPrimaryKey(
			searchConfigurationId);

		Session session = null;

		try {
			session = openSession();

			SearchConfiguration[] array = new SearchConfigurationImpl[3];

			array[0] = getByG_S_T_PrevAndNext(
				session, searchConfiguration, groupId, status, type,
				orderByComparator, true);

			array[1] = searchConfiguration;

			array[2] = getByG_S_T_PrevAndNext(
				session, searchConfiguration, groupId, status, type,
				orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected SearchConfiguration getByG_S_T_PrevAndNext(
		Session session, SearchConfiguration searchConfiguration, long groupId,
		int status, int type,
		OrderByComparator<SearchConfiguration> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				6 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(5);
		}

		sb.append(_SQL_SELECT_SEARCHCONFIGURATION_WHERE);

		sb.append(_FINDER_COLUMN_G_S_T_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_S_T_STATUS_2);

		sb.append(_FINDER_COLUMN_G_S_T_TYPE_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(SearchConfigurationModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(groupId);

		queryPos.add(status);

		queryPos.add(type);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						searchConfiguration)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<SearchConfiguration> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the search configurations that the user has permission to view where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @return the matching search configurations that the user has permission to view
	 */
	@Override
	public List<SearchConfiguration> filterFindByG_S_T(
		long groupId, int status, int type) {

		return filterFindByG_S_T(
			groupId, status, type, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the search configurations that the user has permission to view where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of matching search configurations that the user has permission to view
	 */
	@Override
	public List<SearchConfiguration> filterFindByG_S_T(
		long groupId, int status, int type, int start, int end) {

		return filterFindByG_S_T(groupId, status, type, start, end, null);
	}

	/**
	 * Returns an ordered range of all the search configurations that the user has permissions to view where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching search configurations that the user has permission to view
	 */
	@Override
	public List<SearchConfiguration> filterFindByG_S_T(
		long groupId, int status, int type, int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		if (!InlineSQLHelperUtil.isEnabled(groupId)) {
			return findByG_S_T(
				groupId, status, type, start, end, orderByComparator);
		}

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByFields().length * 2));
		}
		else {
			sb = new StringBundler(6);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_SEARCHCONFIGURATION_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_SEARCHCONFIGURATION_NO_INLINE_DISTINCT_WHERE_1);
		}

		sb.append(_FINDER_COLUMN_G_S_T_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_S_T_STATUS_2);

		sb.append(_FINDER_COLUMN_G_S_T_TYPE_2_SQL);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_SEARCHCONFIGURATION_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			if (getDB().isSupportsInlineDistinct()) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator, true);
			}
			else {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_TABLE, orderByComparator, true);
			}
		}
		else {
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(SearchConfigurationModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(SearchConfigurationModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), SearchConfiguration.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupId);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			if (getDB().isSupportsInlineDistinct()) {
				sqlQuery.addEntity(
					_FILTER_ENTITY_ALIAS, SearchConfigurationImpl.class);
			}
			else {
				sqlQuery.addEntity(
					_FILTER_ENTITY_TABLE, SearchConfigurationImpl.class);
			}

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(groupId);

			queryPos.add(status);

			queryPos.add(type);

			return (List<SearchConfiguration>)QueryUtil.list(
				sqlQuery, getDialect(), start, end);
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	/**
	 * Returns the search configurations before and after the current search configuration in the ordered set of search configurations that the user has permission to view where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param searchConfigurationId the primary key of the current search configuration
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	@Override
	public SearchConfiguration[] filterFindByG_S_T_PrevAndNext(
			long searchConfigurationId, long groupId, int status, int type,
			OrderByComparator<SearchConfiguration> orderByComparator)
		throws NoSuchConfigurationException {

		if (!InlineSQLHelperUtil.isEnabled(groupId)) {
			return findByG_S_T_PrevAndNext(
				searchConfigurationId, groupId, status, type,
				orderByComparator);
		}

		SearchConfiguration searchConfiguration = findByPrimaryKey(
			searchConfigurationId);

		Session session = null;

		try {
			session = openSession();

			SearchConfiguration[] array = new SearchConfigurationImpl[3];

			array[0] = filterGetByG_S_T_PrevAndNext(
				session, searchConfiguration, groupId, status, type,
				orderByComparator, true);

			array[1] = searchConfiguration;

			array[2] = filterGetByG_S_T_PrevAndNext(
				session, searchConfiguration, groupId, status, type,
				orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected SearchConfiguration filterGetByG_S_T_PrevAndNext(
		Session session, SearchConfiguration searchConfiguration, long groupId,
		int status, int type,
		OrderByComparator<SearchConfiguration> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				7 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(6);
		}

		if (getDB().isSupportsInlineDistinct()) {
			sb.append(_FILTER_SQL_SELECT_SEARCHCONFIGURATION_WHERE);
		}
		else {
			sb.append(
				_FILTER_SQL_SELECT_SEARCHCONFIGURATION_NO_INLINE_DISTINCT_WHERE_1);
		}

		sb.append(_FINDER_COLUMN_G_S_T_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_S_T_STATUS_2);

		sb.append(_FINDER_COLUMN_G_S_T_TYPE_2_SQL);

		if (!getDB().isSupportsInlineDistinct()) {
			sb.append(
				_FILTER_SQL_SELECT_SEARCHCONFIGURATION_NO_INLINE_DISTINCT_WHERE_2);
		}

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				if (getDB().isSupportsInlineDistinct()) {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_ALIAS, orderByConditionFields[i],
							true));
				}
				else {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_TABLE, orderByConditionFields[i],
							true));
				}

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				if (getDB().isSupportsInlineDistinct()) {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_ALIAS, orderByFields[i], true));
				}
				else {
					sb.append(
						getColumnName(
							_ORDER_BY_ENTITY_TABLE, orderByFields[i], true));
				}

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			if (getDB().isSupportsInlineDistinct()) {
				sb.append(SearchConfigurationModelImpl.ORDER_BY_JPQL);
			}
			else {
				sb.append(SearchConfigurationModelImpl.ORDER_BY_SQL);
			}
		}

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), SearchConfiguration.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupId);

		SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

		sqlQuery.setFirstResult(0);
		sqlQuery.setMaxResults(2);

		if (getDB().isSupportsInlineDistinct()) {
			sqlQuery.addEntity(
				_FILTER_ENTITY_ALIAS, SearchConfigurationImpl.class);
		}
		else {
			sqlQuery.addEntity(
				_FILTER_ENTITY_TABLE, SearchConfigurationImpl.class);
		}

		QueryPos queryPos = QueryPos.getInstance(sqlQuery);

		queryPos.add(groupId);

		queryPos.add(status);

		queryPos.add(type);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						searchConfiguration)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<SearchConfiguration> list = sqlQuery.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the search configurations where groupId = &#63; and status = &#63; and type = &#63; from the database.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 */
	@Override
	public void removeByG_S_T(long groupId, int status, int type) {
		for (SearchConfiguration searchConfiguration :
				findByG_S_T(
					groupId, status, type, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(searchConfiguration);
		}
	}

	/**
	 * Returns the number of search configurations where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @return the number of matching search configurations
	 */
	@Override
	public int countByG_S_T(long groupId, int status, int type) {
		FinderPath finderPath = _finderPathCountByG_S_T;

		Object[] finderArgs = new Object[] {groupId, status, type};

		Long count = (Long)finderCache.getResult(finderPath, finderArgs, this);

		if (count == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_COUNT_SEARCHCONFIGURATION_WHERE);

			sb.append(_FINDER_COLUMN_G_S_T_GROUPID_2);

			sb.append(_FINDER_COLUMN_G_S_T_STATUS_2);

			sb.append(_FINDER_COLUMN_G_S_T_TYPE_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(groupId);

				queryPos.add(status);

				queryPos.add(type);

				count = (Long)query.uniqueResult();

				finderCache.putResult(finderPath, finderArgs, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(finderPath, finderArgs);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of search configurations that the user has permission to view where groupId = &#63; and status = &#63; and type = &#63;.
	 *
	 * @param groupId the group ID
	 * @param status the status
	 * @param type the type
	 * @return the number of matching search configurations that the user has permission to view
	 */
	@Override
	public int filterCountByG_S_T(long groupId, int status, int type) {
		if (!InlineSQLHelperUtil.isEnabled(groupId)) {
			return countByG_S_T(groupId, status, type);
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_FILTER_SQL_COUNT_SEARCHCONFIGURATION_WHERE);

		sb.append(_FINDER_COLUMN_G_S_T_GROUPID_2);

		sb.append(_FINDER_COLUMN_G_S_T_STATUS_2);

		sb.append(_FINDER_COLUMN_G_S_T_TYPE_2_SQL);

		String sql = InlineSQLHelperUtil.replacePermissionCheck(
			sb.toString(), SearchConfiguration.class.getName(),
			_FILTER_ENTITY_TABLE_FILTER_PK_COLUMN, groupId);

		Session session = null;

		try {
			session = openSession();

			SQLQuery sqlQuery = session.createSynchronizedSQLQuery(sql);

			sqlQuery.addScalar(
				COUNT_COLUMN_NAME, com.liferay.portal.kernel.dao.orm.Type.LONG);

			QueryPos queryPos = QueryPos.getInstance(sqlQuery);

			queryPos.add(groupId);

			queryPos.add(status);

			queryPos.add(type);

			Long count = (Long)sqlQuery.uniqueResult();

			return count.intValue();
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	private static final String _FINDER_COLUMN_G_S_T_GROUPID_2 =
		"searchConfiguration.groupId = ? AND ";

	private static final String _FINDER_COLUMN_G_S_T_STATUS_2 =
		"searchConfiguration.status = ? AND ";

	private static final String _FINDER_COLUMN_G_S_T_TYPE_2 =
		"searchConfiguration.type = ?";

	private static final String _FINDER_COLUMN_G_S_T_TYPE_2_SQL =
		"searchConfiguration.type_ = ?";

	public SearchConfigurationPersistenceImpl() {
		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("uuid", "uuid_");
		dbColumnNames.put("type", "type_");

		setDBColumnNames(dbColumnNames);

		setModelClass(SearchConfiguration.class);

		setModelImplClass(SearchConfigurationImpl.class);
		setModelPKClass(long.class);

		setTable(SearchConfigurationTable.INSTANCE);
	}

	/**
	 * Caches the search configuration in the entity cache if it is enabled.
	 *
	 * @param searchConfiguration the search configuration
	 */
	@Override
	public void cacheResult(SearchConfiguration searchConfiguration) {
		entityCache.putResult(
			entityCacheEnabled, SearchConfigurationImpl.class,
			searchConfiguration.getPrimaryKey(), searchConfiguration);

		finderCache.putResult(
			_finderPathFetchByUUID_G,
			new Object[] {
				searchConfiguration.getUuid(), searchConfiguration.getGroupId()
			},
			searchConfiguration);

		searchConfiguration.resetOriginalValues();
	}

	/**
	 * Caches the search configurations in the entity cache if it is enabled.
	 *
	 * @param searchConfigurations the search configurations
	 */
	@Override
	public void cacheResult(List<SearchConfiguration> searchConfigurations) {
		for (SearchConfiguration searchConfiguration : searchConfigurations) {
			if (entityCache.getResult(
					entityCacheEnabled, SearchConfigurationImpl.class,
					searchConfiguration.getPrimaryKey()) == null) {

				cacheResult(searchConfiguration);
			}
			else {
				searchConfiguration.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all search configurations.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(SearchConfigurationImpl.class);

		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the search configuration.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(SearchConfiguration searchConfiguration) {
		entityCache.removeResult(
			entityCacheEnabled, SearchConfigurationImpl.class,
			searchConfiguration.getPrimaryKey());

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		clearUniqueFindersCache(
			(SearchConfigurationModelImpl)searchConfiguration, true);
	}

	@Override
	public void clearCache(List<SearchConfiguration> searchConfigurations) {
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (SearchConfiguration searchConfiguration : searchConfigurations) {
			entityCache.removeResult(
				entityCacheEnabled, SearchConfigurationImpl.class,
				searchConfiguration.getPrimaryKey());

			clearUniqueFindersCache(
				(SearchConfigurationModelImpl)searchConfiguration, true);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(
				entityCacheEnabled, SearchConfigurationImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		SearchConfigurationModelImpl searchConfigurationModelImpl) {

		Object[] args = new Object[] {
			searchConfigurationModelImpl.getUuid(),
			searchConfigurationModelImpl.getGroupId()
		};

		finderCache.putResult(
			_finderPathCountByUUID_G, args, Long.valueOf(1), false);
		finderCache.putResult(
			_finderPathFetchByUUID_G, args, searchConfigurationModelImpl,
			false);
	}

	protected void clearUniqueFindersCache(
		SearchConfigurationModelImpl searchConfigurationModelImpl,
		boolean clearCurrent) {

		if (clearCurrent) {
			Object[] args = new Object[] {
				searchConfigurationModelImpl.getUuid(),
				searchConfigurationModelImpl.getGroupId()
			};

			finderCache.removeResult(_finderPathCountByUUID_G, args);
			finderCache.removeResult(_finderPathFetchByUUID_G, args);
		}

		if ((searchConfigurationModelImpl.getColumnBitmask() &
			 _finderPathFetchByUUID_G.getColumnBitmask()) != 0) {

			Object[] args = new Object[] {
				searchConfigurationModelImpl.getOriginalUuid(),
				searchConfigurationModelImpl.getOriginalGroupId()
			};

			finderCache.removeResult(_finderPathCountByUUID_G, args);
			finderCache.removeResult(_finderPathFetchByUUID_G, args);
		}
	}

	/**
	 * Creates a new search configuration with the primary key. Does not add the search configuration to the database.
	 *
	 * @param searchConfigurationId the primary key for the new search configuration
	 * @return the new search configuration
	 */
	@Override
	public SearchConfiguration create(long searchConfigurationId) {
		SearchConfiguration searchConfiguration = new SearchConfigurationImpl();

		searchConfiguration.setNew(true);
		searchConfiguration.setPrimaryKey(searchConfigurationId);

		String uuid = PortalUUIDUtil.generate();

		searchConfiguration.setUuid(uuid);

		searchConfiguration.setCompanyId(CompanyThreadLocal.getCompanyId());

		return searchConfiguration;
	}

	/**
	 * Removes the search configuration with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param searchConfigurationId the primary key of the search configuration
	 * @return the search configuration that was removed
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	@Override
	public SearchConfiguration remove(long searchConfigurationId)
		throws NoSuchConfigurationException {

		return remove((Serializable)searchConfigurationId);
	}

	/**
	 * Removes the search configuration with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the search configuration
	 * @return the search configuration that was removed
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	@Override
	public SearchConfiguration remove(Serializable primaryKey)
		throws NoSuchConfigurationException {

		Session session = null;

		try {
			session = openSession();

			SearchConfiguration searchConfiguration =
				(SearchConfiguration)session.get(
					SearchConfigurationImpl.class, primaryKey);

			if (searchConfiguration == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchConfigurationException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(searchConfiguration);
		}
		catch (NoSuchConfigurationException noSuchEntityException) {
			throw noSuchEntityException;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected SearchConfiguration removeImpl(
		SearchConfiguration searchConfiguration) {

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(searchConfiguration)) {
				searchConfiguration = (SearchConfiguration)session.get(
					SearchConfigurationImpl.class,
					searchConfiguration.getPrimaryKeyObj());
			}

			if (searchConfiguration != null) {
				session.delete(searchConfiguration);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (searchConfiguration != null) {
			clearCache(searchConfiguration);
		}

		return searchConfiguration;
	}

	@Override
	public SearchConfiguration updateImpl(
		SearchConfiguration searchConfiguration) {

		boolean isNew = searchConfiguration.isNew();

		if (!(searchConfiguration instanceof SearchConfigurationModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(searchConfiguration.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					searchConfiguration);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in searchConfiguration proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom SearchConfiguration implementation " +
					searchConfiguration.getClass());
		}

		SearchConfigurationModelImpl searchConfigurationModelImpl =
			(SearchConfigurationModelImpl)searchConfiguration;

		if (Validator.isNull(searchConfiguration.getUuid())) {
			String uuid = PortalUUIDUtil.generate();

			searchConfiguration.setUuid(uuid);
		}

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date now = new Date();

		if (isNew && (searchConfiguration.getCreateDate() == null)) {
			if (serviceContext == null) {
				searchConfiguration.setCreateDate(now);
			}
			else {
				searchConfiguration.setCreateDate(
					serviceContext.getCreateDate(now));
			}
		}

		if (!searchConfigurationModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				searchConfiguration.setModifiedDate(now);
			}
			else {
				searchConfiguration.setModifiedDate(
					serviceContext.getModifiedDate(now));
			}
		}

		long userId = GetterUtil.getLong(PrincipalThreadLocal.getName());

		if (userId > 0) {
			long companyId = searchConfiguration.getCompanyId();

			long groupId = searchConfiguration.getGroupId();

			long searchConfigurationId = 0;

			if (!isNew) {
				searchConfigurationId = searchConfiguration.getPrimaryKey();
			}

			try {
				searchConfiguration.setTitle(
					SanitizerUtil.sanitize(
						companyId, groupId, userId,
						SearchConfiguration.class.getName(),
						searchConfigurationId, ContentTypes.TEXT_PLAIN,
						Sanitizer.MODE_ALL, searchConfiguration.getTitle(),
						null));
			}
			catch (SanitizerException sanitizerException) {
				throw new SystemException(sanitizerException);
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (searchConfiguration.isNew()) {
				session.save(searchConfiguration);

				searchConfiguration.setNew(false);
			}
			else {
				searchConfiguration = (SearchConfiguration)session.merge(
					searchConfiguration);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (!_columnBitmaskEnabled) {
			finderCache.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}
		else if (isNew) {
			Object[] args = new Object[] {
				searchConfigurationModelImpl.getUuid()
			};

			finderCache.removeResult(_finderPathCountByUuid, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByUuid, args);

			args = new Object[] {
				searchConfigurationModelImpl.getUuid(),
				searchConfigurationModelImpl.getCompanyId()
			};

			finderCache.removeResult(_finderPathCountByUuid_C, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByUuid_C, args);

			args = new Object[] {searchConfigurationModelImpl.getGroupId()};

			finderCache.removeResult(_finderPathCountByGroupId, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByGroupId, args);

			args = new Object[] {searchConfigurationModelImpl.getCompanyId()};

			finderCache.removeResult(_finderPathCountByCompanyId, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByCompanyId, args);

			args = new Object[] {
				searchConfigurationModelImpl.getGroupId(),
				searchConfigurationModelImpl.getStatus()
			};

			finderCache.removeResult(_finderPathCountByG_S, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByG_S, args);

			args = new Object[] {
				searchConfigurationModelImpl.getGroupId(),
				searchConfigurationModelImpl.getType()
			};

			finderCache.removeResult(_finderPathCountByG_T, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByG_T, args);

			args = new Object[] {
				searchConfigurationModelImpl.getGroupId(),
				searchConfigurationModelImpl.getStatus(),
				searchConfigurationModelImpl.getType()
			};

			finderCache.removeResult(_finderPathCountByG_S_T, args);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindByG_S_T, args);

			finderCache.removeResult(_finderPathCountAll, FINDER_ARGS_EMPTY);
			finderCache.removeResult(
				_finderPathWithoutPaginationFindAll, FINDER_ARGS_EMPTY);
		}
		else {
			if ((searchConfigurationModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByUuid.getColumnBitmask()) !=
					 0) {

				Object[] args = new Object[] {
					searchConfigurationModelImpl.getOriginalUuid()
				};

				finderCache.removeResult(_finderPathCountByUuid, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByUuid, args);

				args = new Object[] {searchConfigurationModelImpl.getUuid()};

				finderCache.removeResult(_finderPathCountByUuid, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByUuid, args);
			}

			if ((searchConfigurationModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByUuid_C.getColumnBitmask()) !=
					 0) {

				Object[] args = new Object[] {
					searchConfigurationModelImpl.getOriginalUuid(),
					searchConfigurationModelImpl.getOriginalCompanyId()
				};

				finderCache.removeResult(_finderPathCountByUuid_C, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByUuid_C, args);

				args = new Object[] {
					searchConfigurationModelImpl.getUuid(),
					searchConfigurationModelImpl.getCompanyId()
				};

				finderCache.removeResult(_finderPathCountByUuid_C, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByUuid_C, args);
			}

			if ((searchConfigurationModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByGroupId.
					 getColumnBitmask()) != 0) {

				Object[] args = new Object[] {
					searchConfigurationModelImpl.getOriginalGroupId()
				};

				finderCache.removeResult(_finderPathCountByGroupId, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByGroupId, args);

				args = new Object[] {searchConfigurationModelImpl.getGroupId()};

				finderCache.removeResult(_finderPathCountByGroupId, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByGroupId, args);
			}

			if ((searchConfigurationModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByCompanyId.
					 getColumnBitmask()) != 0) {

				Object[] args = new Object[] {
					searchConfigurationModelImpl.getOriginalCompanyId()
				};

				finderCache.removeResult(_finderPathCountByCompanyId, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByCompanyId, args);

				args = new Object[] {
					searchConfigurationModelImpl.getCompanyId()
				};

				finderCache.removeResult(_finderPathCountByCompanyId, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByCompanyId, args);
			}

			if ((searchConfigurationModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByG_S.getColumnBitmask()) !=
					 0) {

				Object[] args = new Object[] {
					searchConfigurationModelImpl.getOriginalGroupId(),
					searchConfigurationModelImpl.getOriginalStatus()
				};

				finderCache.removeResult(_finderPathCountByG_S, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByG_S, args);

				args = new Object[] {
					searchConfigurationModelImpl.getGroupId(),
					searchConfigurationModelImpl.getStatus()
				};

				finderCache.removeResult(_finderPathCountByG_S, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByG_S, args);
			}

			if ((searchConfigurationModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByG_T.getColumnBitmask()) !=
					 0) {

				Object[] args = new Object[] {
					searchConfigurationModelImpl.getOriginalGroupId(),
					searchConfigurationModelImpl.getOriginalType()
				};

				finderCache.removeResult(_finderPathCountByG_T, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByG_T, args);

				args = new Object[] {
					searchConfigurationModelImpl.getGroupId(),
					searchConfigurationModelImpl.getType()
				};

				finderCache.removeResult(_finderPathCountByG_T, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByG_T, args);
			}

			if ((searchConfigurationModelImpl.getColumnBitmask() &
				 _finderPathWithoutPaginationFindByG_S_T.getColumnBitmask()) !=
					 0) {

				Object[] args = new Object[] {
					searchConfigurationModelImpl.getOriginalGroupId(),
					searchConfigurationModelImpl.getOriginalStatus(),
					searchConfigurationModelImpl.getOriginalType()
				};

				finderCache.removeResult(_finderPathCountByG_S_T, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByG_S_T, args);

				args = new Object[] {
					searchConfigurationModelImpl.getGroupId(),
					searchConfigurationModelImpl.getStatus(),
					searchConfigurationModelImpl.getType()
				};

				finderCache.removeResult(_finderPathCountByG_S_T, args);
				finderCache.removeResult(
					_finderPathWithoutPaginationFindByG_S_T, args);
			}
		}

		entityCache.putResult(
			entityCacheEnabled, SearchConfigurationImpl.class,
			searchConfiguration.getPrimaryKey(), searchConfiguration, false);

		clearUniqueFindersCache(searchConfigurationModelImpl, false);
		cacheUniqueFindersCache(searchConfigurationModelImpl);

		searchConfiguration.resetOriginalValues();

		return searchConfiguration;
	}

	/**
	 * Returns the search configuration with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the search configuration
	 * @return the search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	@Override
	public SearchConfiguration findByPrimaryKey(Serializable primaryKey)
		throws NoSuchConfigurationException {

		SearchConfiguration searchConfiguration = fetchByPrimaryKey(primaryKey);

		if (searchConfiguration == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchConfigurationException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return searchConfiguration;
	}

	/**
	 * Returns the search configuration with the primary key or throws a <code>NoSuchConfigurationException</code> if it could not be found.
	 *
	 * @param searchConfigurationId the primary key of the search configuration
	 * @return the search configuration
	 * @throws NoSuchConfigurationException if a search configuration with the primary key could not be found
	 */
	@Override
	public SearchConfiguration findByPrimaryKey(long searchConfigurationId)
		throws NoSuchConfigurationException {

		return findByPrimaryKey((Serializable)searchConfigurationId);
	}

	/**
	 * Returns the search configuration with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param searchConfigurationId the primary key of the search configuration
	 * @return the search configuration, or <code>null</code> if a search configuration with the primary key could not be found
	 */
	@Override
	public SearchConfiguration fetchByPrimaryKey(long searchConfigurationId) {
		return fetchByPrimaryKey((Serializable)searchConfigurationId);
	}

	/**
	 * Returns all the search configurations.
	 *
	 * @return the search configurations
	 */
	@Override
	public List<SearchConfiguration> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the search configurations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @return the range of search configurations
	 */
	@Override
	public List<SearchConfiguration> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the search configurations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of search configurations
	 */
	@Override
	public List<SearchConfiguration> findAll(
		int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the search configurations.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>SearchConfigurationModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of search configurations
	 * @param end the upper bound of the range of search configurations (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of search configurations
	 */
	@Override
	public List<SearchConfiguration> findAll(
		int start, int end,
		OrderByComparator<SearchConfiguration> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindAll;
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<SearchConfiguration> list = null;

		if (useFinderCache) {
			list = (List<SearchConfiguration>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_SEARCHCONFIGURATION);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_SEARCHCONFIGURATION;

				sql = sql.concat(SearchConfigurationModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<SearchConfiguration>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				if (useFinderCache) {
					finderCache.removeResult(finderPath, finderArgs);
				}

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the search configurations from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (SearchConfiguration searchConfiguration : findAll()) {
			remove(searchConfiguration);
		}
	}

	/**
	 * Returns the number of search configurations.
	 *
	 * @return the number of search configurations
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(
					_SQL_COUNT_SEARCHCONFIGURATION);

				count = (Long)query.uniqueResult();

				finderCache.putResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY, count);
			}
			catch (Exception exception) {
				finderCache.removeResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY);

				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	public Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "searchConfigurationId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_SEARCHCONFIGURATION;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return SearchConfigurationModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the search configuration persistence.
	 */
	@Activate
	public void activate() {
		SearchConfigurationModelImpl.setEntityCacheEnabled(entityCacheEnabled);
		SearchConfigurationModelImpl.setFinderCacheEnabled(finderCacheEnabled);

		_finderPathWithPaginationFindAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			SearchConfigurationImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0]);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			SearchConfigurationImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll",
			new String[0]);

		_finderPathCountAll = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0]);

		_finderPathWithPaginationFindByUuid = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			SearchConfigurationImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid",
			new String[] {
				String.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByUuid = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			SearchConfigurationImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid",
			new String[] {String.class.getName()},
			SearchConfigurationModelImpl.UUID_COLUMN_BITMASK |
			SearchConfigurationModelImpl.CREATEDATE_COLUMN_BITMASK);

		_finderPathCountByUuid = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid",
			new String[] {String.class.getName()});

		_finderPathFetchByUUID_G = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			SearchConfigurationImpl.class, FINDER_CLASS_NAME_ENTITY,
			"fetchByUUID_G",
			new String[] {String.class.getName(), Long.class.getName()},
			SearchConfigurationModelImpl.UUID_COLUMN_BITMASK |
			SearchConfigurationModelImpl.GROUPID_COLUMN_BITMASK);

		_finderPathCountByUUID_G = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUUID_G",
			new String[] {String.class.getName(), Long.class.getName()});

		_finderPathWithPaginationFindByUuid_C = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			SearchConfigurationImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByUuid_C",
			new String[] {
				String.class.getName(), Long.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByUuid_C = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			SearchConfigurationImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUuid_C",
			new String[] {String.class.getName(), Long.class.getName()},
			SearchConfigurationModelImpl.UUID_COLUMN_BITMASK |
			SearchConfigurationModelImpl.COMPANYID_COLUMN_BITMASK |
			SearchConfigurationModelImpl.CREATEDATE_COLUMN_BITMASK);

		_finderPathCountByUuid_C = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUuid_C",
			new String[] {String.class.getName(), Long.class.getName()});

		_finderPathWithPaginationFindByGroupId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			SearchConfigurationImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByGroupId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByGroupId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			SearchConfigurationImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByGroupId",
			new String[] {Long.class.getName()},
			SearchConfigurationModelImpl.GROUPID_COLUMN_BITMASK |
			SearchConfigurationModelImpl.CREATEDATE_COLUMN_BITMASK);

		_finderPathCountByGroupId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByGroupId",
			new String[] {Long.class.getName()});

		_finderPathWithPaginationFindByCompanyId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			SearchConfigurationImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCompanyId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByCompanyId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			SearchConfigurationImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByCompanyId",
			new String[] {Long.class.getName()},
			SearchConfigurationModelImpl.COMPANYID_COLUMN_BITMASK |
			SearchConfigurationModelImpl.CREATEDATE_COLUMN_BITMASK);

		_finderPathCountByCompanyId = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByCompanyId",
			new String[] {Long.class.getName()});

		_finderPathWithPaginationFindByG_S = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			SearchConfigurationImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByG_S",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByG_S = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			SearchConfigurationImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByG_S",
			new String[] {Long.class.getName(), Integer.class.getName()},
			SearchConfigurationModelImpl.GROUPID_COLUMN_BITMASK |
			SearchConfigurationModelImpl.STATUS_COLUMN_BITMASK |
			SearchConfigurationModelImpl.CREATEDATE_COLUMN_BITMASK);

		_finderPathCountByG_S = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_S",
			new String[] {Long.class.getName(), Integer.class.getName()});

		_finderPathWithPaginationFindByG_T = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			SearchConfigurationImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByG_T",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByG_T = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			SearchConfigurationImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByG_T",
			new String[] {Long.class.getName(), Integer.class.getName()},
			SearchConfigurationModelImpl.GROUPID_COLUMN_BITMASK |
			SearchConfigurationModelImpl.TYPE_COLUMN_BITMASK |
			SearchConfigurationModelImpl.CREATEDATE_COLUMN_BITMASK);

		_finderPathCountByG_T = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_T",
			new String[] {Long.class.getName(), Integer.class.getName()});

		_finderPathWithPaginationFindByG_S_T = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			SearchConfigurationImpl.class,
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByG_S_T",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			});

		_finderPathWithoutPaginationFindByG_S_T = new FinderPath(
			entityCacheEnabled, finderCacheEnabled,
			SearchConfigurationImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByG_S_T",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName()
			},
			SearchConfigurationModelImpl.GROUPID_COLUMN_BITMASK |
			SearchConfigurationModelImpl.STATUS_COLUMN_BITMASK |
			SearchConfigurationModelImpl.TYPE_COLUMN_BITMASK |
			SearchConfigurationModelImpl.CREATEDATE_COLUMN_BITMASK);

		_finderPathCountByG_S_T = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByG_S_T",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName()
			});
	}

	@Deactivate
	public void deactivate() {
		entityCache.removeCache(SearchConfigurationImpl.class.getName());
		finderCache.removeCache(FINDER_CLASS_NAME_ENTITY);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		finderCache.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	@Reference(
		target = SearchPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
		super.setConfiguration(configuration);

		_columnBitmaskEnabled = GetterUtil.getBoolean(
			configuration.get(
				"value.object.column.bitmask.enabled.com.liferay.portal.search.tuning.gsearch.configuration.model.SearchConfiguration"),
			true);
	}

	@Override
	@Reference(
		target = SearchPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = SearchPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	private boolean _columnBitmaskEnabled;

	@Reference
	protected EntityCache entityCache;

	@Reference
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_SEARCHCONFIGURATION =
		"SELECT searchConfiguration FROM SearchConfiguration searchConfiguration";

	private static final String _SQL_SELECT_SEARCHCONFIGURATION_WHERE =
		"SELECT searchConfiguration FROM SearchConfiguration searchConfiguration WHERE ";

	private static final String _SQL_COUNT_SEARCHCONFIGURATION =
		"SELECT COUNT(searchConfiguration) FROM SearchConfiguration searchConfiguration";

	private static final String _SQL_COUNT_SEARCHCONFIGURATION_WHERE =
		"SELECT COUNT(searchConfiguration) FROM SearchConfiguration searchConfiguration WHERE ";

	private static final String _FILTER_ENTITY_TABLE_FILTER_PK_COLUMN =
		"searchConfiguration.searchConfigurationId";

	private static final String _FILTER_SQL_SELECT_SEARCHCONFIGURATION_WHERE =
		"SELECT DISTINCT {searchConfiguration.*} FROM SearchConfiguration searchConfiguration WHERE ";

	private static final String
		_FILTER_SQL_SELECT_SEARCHCONFIGURATION_NO_INLINE_DISTINCT_WHERE_1 =
			"SELECT {SearchConfiguration.*} FROM (SELECT DISTINCT searchConfiguration.searchConfigurationId FROM SearchConfiguration searchConfiguration WHERE ";

	private static final String
		_FILTER_SQL_SELECT_SEARCHCONFIGURATION_NO_INLINE_DISTINCT_WHERE_2 =
			") TEMP_TABLE INNER JOIN SearchConfiguration ON TEMP_TABLE.searchConfigurationId = SearchConfiguration.searchConfigurationId";

	private static final String _FILTER_SQL_COUNT_SEARCHCONFIGURATION_WHERE =
		"SELECT COUNT(DISTINCT searchConfiguration.searchConfigurationId) AS COUNT_VALUE FROM SearchConfiguration searchConfiguration WHERE ";

	private static final String _FILTER_ENTITY_ALIAS = "searchConfiguration";

	private static final String _FILTER_ENTITY_TABLE = "SearchConfiguration";

	private static final String _ORDER_BY_ENTITY_ALIAS = "searchConfiguration.";

	private static final String _ORDER_BY_ENTITY_TABLE = "SearchConfiguration.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No SearchConfiguration exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No SearchConfiguration exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		SearchConfigurationPersistenceImpl.class);

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"uuid", "type"});

	static {
		try {
			Class.forName(SearchPersistenceConstants.class.getName());
		}
		catch (ClassNotFoundException classNotFoundException) {
			throw new ExceptionInInitializerError(classNotFoundException);
		}
	}

}