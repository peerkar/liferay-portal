/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.generative.ai.task.service.persistence.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.generative.ai.task.exception.DuplicateTaskDefinitionExternalReferenceCodeException;
import com.liferay.generative.ai.task.exception.NoSuchTaskDefinitionException;
import com.liferay.generative.ai.task.model.TaskDefinition;
import com.liferay.generative.ai.task.service.TaskDefinitionLocalServiceUtil;
import com.liferay.generative.ai.task.service.persistence.TaskDefinitionPersistence;
import com.liferay.generative.ai.task.service.persistence.TaskDefinitionUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.util.IntegerWrapper;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.OrderByComparatorFactoryUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PersistenceTestRule;
import com.liferay.portal.test.rule.TransactionalTestRule;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @generated
 */
@RunWith(Arquillian.class)
public class TaskDefinitionPersistenceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(), PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(
				Propagation.REQUIRED,
				"com.liferay.generative.ai.task.service"));

	@Before
	public void setUp() {
		_persistence = TaskDefinitionUtil.getPersistence();

		Class<?> clazz = _persistence.getClass();

		_dynamicQueryClassLoader = clazz.getClassLoader();
	}

	@After
	public void tearDown() throws Exception {
		Iterator<TaskDefinition> iterator = _taskDefinitions.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testCreate() throws Exception {
		long pk = RandomTestUtil.nextLong();

		TaskDefinition taskDefinition = _persistence.create(pk);

		Assert.assertNotNull(taskDefinition);

		Assert.assertEquals(taskDefinition.getPrimaryKey(), pk);
	}

	@Test
	public void testRemove() throws Exception {
		TaskDefinition newTaskDefinition = addTaskDefinition();

		_persistence.remove(newTaskDefinition);

		TaskDefinition existingTaskDefinition = _persistence.fetchByPrimaryKey(
			newTaskDefinition.getPrimaryKey());

		Assert.assertNull(existingTaskDefinition);
	}

	@Test
	public void testUpdateNew() throws Exception {
		addTaskDefinition();
	}

	@Test
	public void testUpdateExisting() throws Exception {
		long pk = RandomTestUtil.nextLong();

		TaskDefinition newTaskDefinition = _persistence.create(pk);

		newTaskDefinition.setMvccVersion(RandomTestUtil.nextLong());

		newTaskDefinition.setUuid(RandomTestUtil.randomString());

		newTaskDefinition.setExternalReferenceCode(
			RandomTestUtil.randomString());

		newTaskDefinition.setCompanyId(RandomTestUtil.nextLong());

		newTaskDefinition.setUserId(RandomTestUtil.nextLong());

		newTaskDefinition.setUserName(RandomTestUtil.randomString());

		newTaskDefinition.setCreateDate(RandomTestUtil.nextDate());

		newTaskDefinition.setModifiedDate(RandomTestUtil.nextDate());

		newTaskDefinition.setConfigurationJSON(RandomTestUtil.randomString());

		newTaskDefinition.setDescription(RandomTestUtil.randomString());

		newTaskDefinition.setReadOnly(RandomTestUtil.randomBoolean());

		newTaskDefinition.setSchemaVersion(RandomTestUtil.randomString());

		newTaskDefinition.setTitle(RandomTestUtil.randomString());

		newTaskDefinition.setVersion(RandomTestUtil.randomString());

		newTaskDefinition.setStatus(RandomTestUtil.nextInt());

		newTaskDefinition.setStatusByUserId(RandomTestUtil.nextLong());

		newTaskDefinition.setStatusByUserName(RandomTestUtil.randomString());

		newTaskDefinition.setStatusDate(RandomTestUtil.nextDate());

		_taskDefinitions.add(_persistence.update(newTaskDefinition));

		TaskDefinition existingTaskDefinition = _persistence.findByPrimaryKey(
			newTaskDefinition.getPrimaryKey());

		Assert.assertEquals(
			existingTaskDefinition.getMvccVersion(),
			newTaskDefinition.getMvccVersion());
		Assert.assertEquals(
			existingTaskDefinition.getUuid(), newTaskDefinition.getUuid());
		Assert.assertEquals(
			existingTaskDefinition.getExternalReferenceCode(),
			newTaskDefinition.getExternalReferenceCode());
		Assert.assertEquals(
			existingTaskDefinition.getTaskDefinitionId(),
			newTaskDefinition.getTaskDefinitionId());
		Assert.assertEquals(
			existingTaskDefinition.getCompanyId(),
			newTaskDefinition.getCompanyId());
		Assert.assertEquals(
			existingTaskDefinition.getUserId(), newTaskDefinition.getUserId());
		Assert.assertEquals(
			existingTaskDefinition.getUserName(),
			newTaskDefinition.getUserName());
		Assert.assertEquals(
			Time.getShortTimestamp(existingTaskDefinition.getCreateDate()),
			Time.getShortTimestamp(newTaskDefinition.getCreateDate()));
		Assert.assertEquals(
			Time.getShortTimestamp(existingTaskDefinition.getModifiedDate()),
			Time.getShortTimestamp(newTaskDefinition.getModifiedDate()));
		Assert.assertEquals(
			existingTaskDefinition.getConfigurationJSON(),
			newTaskDefinition.getConfigurationJSON());
		Assert.assertEquals(
			existingTaskDefinition.getDescription(),
			newTaskDefinition.getDescription());
		Assert.assertEquals(
			existingTaskDefinition.isReadOnly(),
			newTaskDefinition.isReadOnly());
		Assert.assertEquals(
			existingTaskDefinition.getSchemaVersion(),
			newTaskDefinition.getSchemaVersion());
		Assert.assertEquals(
			existingTaskDefinition.getTitle(), newTaskDefinition.getTitle());
		Assert.assertEquals(
			existingTaskDefinition.getVersion(),
			newTaskDefinition.getVersion());
		Assert.assertEquals(
			existingTaskDefinition.getStatus(), newTaskDefinition.getStatus());
		Assert.assertEquals(
			existingTaskDefinition.getStatusByUserId(),
			newTaskDefinition.getStatusByUserId());
		Assert.assertEquals(
			existingTaskDefinition.getStatusByUserName(),
			newTaskDefinition.getStatusByUserName());
		Assert.assertEquals(
			Time.getShortTimestamp(existingTaskDefinition.getStatusDate()),
			Time.getShortTimestamp(newTaskDefinition.getStatusDate()));
	}

	@Test(
		expected = DuplicateTaskDefinitionExternalReferenceCodeException.class
	)
	public void testUpdateWithExistingExternalReferenceCode() throws Exception {
		TaskDefinition taskDefinition = addTaskDefinition();

		TaskDefinition newTaskDefinition = addTaskDefinition();

		newTaskDefinition.setCompanyId(taskDefinition.getCompanyId());

		newTaskDefinition = _persistence.update(newTaskDefinition);

		Session session = _persistence.getCurrentSession();

		session.evict(newTaskDefinition);

		newTaskDefinition.setExternalReferenceCode(
			taskDefinition.getExternalReferenceCode());

		_persistence.update(newTaskDefinition);
	}

	@Test
	public void testCountByUuid() throws Exception {
		_persistence.countByUuid("");

		_persistence.countByUuid("null");

		_persistence.countByUuid((String)null);
	}

	@Test
	public void testCountByUuid_C() throws Exception {
		_persistence.countByUuid_C("", RandomTestUtil.nextLong());

		_persistence.countByUuid_C("null", 0L);

		_persistence.countByUuid_C((String)null, 0L);
	}

	@Test
	public void testCountByCompanyId() throws Exception {
		_persistence.countByCompanyId(RandomTestUtil.nextLong());

		_persistence.countByCompanyId(0L);
	}

	@Test
	public void testCountByC_R() throws Exception {
		_persistence.countByC_R(
			RandomTestUtil.nextLong(), RandomTestUtil.randomBoolean());

		_persistence.countByC_R(0L, RandomTestUtil.randomBoolean());
	}

	@Test
	public void testCountByERC_C() throws Exception {
		_persistence.countByERC_C("", RandomTestUtil.nextLong());

		_persistence.countByERC_C("null", 0L);

		_persistence.countByERC_C((String)null, 0L);
	}

	@Test
	public void testFindByPrimaryKeyExisting() throws Exception {
		TaskDefinition newTaskDefinition = addTaskDefinition();

		TaskDefinition existingTaskDefinition = _persistence.findByPrimaryKey(
			newTaskDefinition.getPrimaryKey());

		Assert.assertEquals(existingTaskDefinition, newTaskDefinition);
	}

	@Test(expected = NoSuchTaskDefinitionException.class)
	public void testFindByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		_persistence.findByPrimaryKey(pk);
	}

	@Test
	public void testFindAll() throws Exception {
		_persistence.findAll(
			QueryUtil.ALL_POS, QueryUtil.ALL_POS, getOrderByComparator());
	}

	protected OrderByComparator<TaskDefinition> getOrderByComparator() {
		return OrderByComparatorFactoryUtil.create(
			"TaskDefinition", "mvccVersion", true, "uuid", true,
			"externalReferenceCode", true, "taskDefinitionId", true,
			"companyId", true, "userId", true, "userName", true, "createDate",
			true, "modifiedDate", true, "description", true, "readOnly", true,
			"schemaVersion", true, "title", true, "version", true, "status",
			true, "statusByUserId", true, "statusByUserName", true,
			"statusDate", true);
	}

	@Test
	public void testFetchByPrimaryKeyExisting() throws Exception {
		TaskDefinition newTaskDefinition = addTaskDefinition();

		TaskDefinition existingTaskDefinition = _persistence.fetchByPrimaryKey(
			newTaskDefinition.getPrimaryKey());

		Assert.assertEquals(existingTaskDefinition, newTaskDefinition);
	}

	@Test
	public void testFetchByPrimaryKeyMissing() throws Exception {
		long pk = RandomTestUtil.nextLong();

		TaskDefinition missingTaskDefinition = _persistence.fetchByPrimaryKey(
			pk);

		Assert.assertNull(missingTaskDefinition);
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereAllPrimaryKeysExist()
		throws Exception {

		TaskDefinition newTaskDefinition1 = addTaskDefinition();
		TaskDefinition newTaskDefinition2 = addTaskDefinition();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newTaskDefinition1.getPrimaryKey());
		primaryKeys.add(newTaskDefinition2.getPrimaryKey());

		Map<Serializable, TaskDefinition> taskDefinitions =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(2, taskDefinitions.size());
		Assert.assertEquals(
			newTaskDefinition1,
			taskDefinitions.get(newTaskDefinition1.getPrimaryKey()));
		Assert.assertEquals(
			newTaskDefinition2,
			taskDefinitions.get(newTaskDefinition2.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereNoPrimaryKeysExist()
		throws Exception {

		long pk1 = RandomTestUtil.nextLong();

		long pk2 = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(pk1);
		primaryKeys.add(pk2);

		Map<Serializable, TaskDefinition> taskDefinitions =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(taskDefinitions.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithMultiplePrimaryKeysWhereSomePrimaryKeysExist()
		throws Exception {

		TaskDefinition newTaskDefinition = addTaskDefinition();

		long pk = RandomTestUtil.nextLong();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newTaskDefinition.getPrimaryKey());
		primaryKeys.add(pk);

		Map<Serializable, TaskDefinition> taskDefinitions =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, taskDefinitions.size());
		Assert.assertEquals(
			newTaskDefinition,
			taskDefinitions.get(newTaskDefinition.getPrimaryKey()));
	}

	@Test
	public void testFetchByPrimaryKeysWithNoPrimaryKeys() throws Exception {
		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		Map<Serializable, TaskDefinition> taskDefinitions =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertTrue(taskDefinitions.isEmpty());
	}

	@Test
	public void testFetchByPrimaryKeysWithOnePrimaryKey() throws Exception {
		TaskDefinition newTaskDefinition = addTaskDefinition();

		Set<Serializable> primaryKeys = new HashSet<Serializable>();

		primaryKeys.add(newTaskDefinition.getPrimaryKey());

		Map<Serializable, TaskDefinition> taskDefinitions =
			_persistence.fetchByPrimaryKeys(primaryKeys);

		Assert.assertEquals(1, taskDefinitions.size());
		Assert.assertEquals(
			newTaskDefinition,
			taskDefinitions.get(newTaskDefinition.getPrimaryKey()));
	}

	@Test
	public void testActionableDynamicQuery() throws Exception {
		final IntegerWrapper count = new IntegerWrapper();

		ActionableDynamicQuery actionableDynamicQuery =
			TaskDefinitionLocalServiceUtil.getActionableDynamicQuery();

		actionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<TaskDefinition>() {

				@Override
				public void performAction(TaskDefinition taskDefinition) {
					Assert.assertNotNull(taskDefinition);

					count.increment();
				}

			});

		actionableDynamicQuery.performActions();

		Assert.assertEquals(count.getValue(), _persistence.countAll());
	}

	@Test
	public void testDynamicQueryByPrimaryKeyExisting() throws Exception {
		TaskDefinition newTaskDefinition = addTaskDefinition();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			TaskDefinition.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"taskDefinitionId", newTaskDefinition.getTaskDefinitionId()));

		List<TaskDefinition> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(1, result.size());

		TaskDefinition existingTaskDefinition = result.get(0);

		Assert.assertEquals(existingTaskDefinition, newTaskDefinition);
	}

	@Test
	public void testDynamicQueryByPrimaryKeyMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			TaskDefinition.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"taskDefinitionId", RandomTestUtil.nextLong()));

		List<TaskDefinition> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testDynamicQueryByProjectionExisting() throws Exception {
		TaskDefinition newTaskDefinition = addTaskDefinition();

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			TaskDefinition.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("taskDefinitionId"));

		Object newTaskDefinitionId = newTaskDefinition.getTaskDefinitionId();

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"taskDefinitionId", new Object[] {newTaskDefinitionId}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(1, result.size());

		Object existingTaskDefinitionId = result.get(0);

		Assert.assertEquals(existingTaskDefinitionId, newTaskDefinitionId);
	}

	@Test
	public void testDynamicQueryByProjectionMissing() throws Exception {
		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			TaskDefinition.class, _dynamicQueryClassLoader);

		dynamicQuery.setProjection(
			ProjectionFactoryUtil.property("taskDefinitionId"));

		dynamicQuery.add(
			RestrictionsFactoryUtil.in(
				"taskDefinitionId", new Object[] {RandomTestUtil.nextLong()}));

		List<Object> result = _persistence.findWithDynamicQuery(dynamicQuery);

		Assert.assertEquals(0, result.size());
	}

	@Test
	public void testResetOriginalValues() throws Exception {
		TaskDefinition newTaskDefinition = addTaskDefinition();

		_persistence.clearCache();

		_assertOriginalValues(
			_persistence.findByPrimaryKey(newTaskDefinition.getPrimaryKey()));
	}

	@Test
	public void testResetOriginalValuesWithDynamicQueryLoadFromDatabase()
		throws Exception {

		_testResetOriginalValuesWithDynamicQuery(true);
	}

	@Test
	public void testResetOriginalValuesWithDynamicQueryLoadFromSession()
		throws Exception {

		_testResetOriginalValuesWithDynamicQuery(false);
	}

	private void _testResetOriginalValuesWithDynamicQuery(boolean clearSession)
		throws Exception {

		TaskDefinition newTaskDefinition = addTaskDefinition();

		if (clearSession) {
			Session session = _persistence.openSession();

			session.flush();

			session.clear();
		}

		DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(
			TaskDefinition.class, _dynamicQueryClassLoader);

		dynamicQuery.add(
			RestrictionsFactoryUtil.eq(
				"taskDefinitionId", newTaskDefinition.getTaskDefinitionId()));

		List<TaskDefinition> result = _persistence.findWithDynamicQuery(
			dynamicQuery);

		_assertOriginalValues(result.get(0));
	}

	private void _assertOriginalValues(TaskDefinition taskDefinition) {
		Assert.assertEquals(
			taskDefinition.getExternalReferenceCode(),
			ReflectionTestUtil.invoke(
				taskDefinition, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "externalReferenceCode"));
		Assert.assertEquals(
			Long.valueOf(taskDefinition.getCompanyId()),
			ReflectionTestUtil.<Long>invoke(
				taskDefinition, "getColumnOriginalValue",
				new Class<?>[] {String.class}, "companyId"));
	}

	protected TaskDefinition addTaskDefinition() throws Exception {
		long pk = RandomTestUtil.nextLong();

		TaskDefinition taskDefinition = _persistence.create(pk);

		taskDefinition.setMvccVersion(RandomTestUtil.nextLong());

		taskDefinition.setUuid(RandomTestUtil.randomString());

		taskDefinition.setExternalReferenceCode(RandomTestUtil.randomString());

		taskDefinition.setCompanyId(RandomTestUtil.nextLong());

		taskDefinition.setUserId(RandomTestUtil.nextLong());

		taskDefinition.setUserName(RandomTestUtil.randomString());

		taskDefinition.setCreateDate(RandomTestUtil.nextDate());

		taskDefinition.setModifiedDate(RandomTestUtil.nextDate());

		taskDefinition.setConfigurationJSON(RandomTestUtil.randomString());

		taskDefinition.setDescription(RandomTestUtil.randomString());

		taskDefinition.setReadOnly(RandomTestUtil.randomBoolean());

		taskDefinition.setSchemaVersion(RandomTestUtil.randomString());

		taskDefinition.setTitle(RandomTestUtil.randomString());

		taskDefinition.setVersion(RandomTestUtil.randomString());

		taskDefinition.setStatus(RandomTestUtil.nextInt());

		taskDefinition.setStatusByUserId(RandomTestUtil.nextLong());

		taskDefinition.setStatusByUserName(RandomTestUtil.randomString());

		taskDefinition.setStatusDate(RandomTestUtil.nextDate());

		_taskDefinitions.add(_persistence.update(taskDefinition));

		return taskDefinition;
	}

	private List<TaskDefinition> _taskDefinitions =
		new ArrayList<TaskDefinition>();
	private TaskDefinitionPersistence _persistence;
	private ClassLoader _dynamicQueryClassLoader;

}