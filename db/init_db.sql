/*
 Navicat Premium Data Transfer

 Source Server         : 本地连接
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 127.0.0.1:3306
 Source Schema         : flow-demo

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 15/11/2021 16:04:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for approve_role
-- ----------------------------
DROP TABLE IF EXISTS `approve_role`;
CREATE TABLE `approve_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '数据ID',
  `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务数据ID',
  `tenant_id` int NULL DEFAULT NULL COMMENT '租户ID',
  `product_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品ID',
  `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审批角色名称',
  `account_number` int NULL DEFAULT NULL COMMENT '账号数量',
  `account_name` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '账号名称(逗号分割)',
  `approve_number` int NULL DEFAULT NULL COMMENT '应用的审批流数据',
  `approve_name` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '审批流名称(逗号分割)',
  `temporary` tinyint(1) NULL DEFAULT 0 COMMENT '临时(0-否，1-是)',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除标识(0-否，1-是)',
  `creator_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `gmt_create` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `modifier_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `gmt_modified` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 312 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '审批角色配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for approve_user
-- ----------------------------
DROP TABLE IF EXISTS `approve_user`;
CREATE TABLE `approve_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '数据ID',
  `biz_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务数据ID',
  `role_group_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审批角色组ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `approve_department` json NULL COMMENT '审批部门ID',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除状态(0-否，1-是)',
  `creator_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `gmt_create` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `modifier_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `gmt_modified` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `id`(`id`) USING BTREE,
  INDEX `roleGroupId`(`role_group_id`) USING BTREE,
  INDEX `userId`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 747 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '审批角色配置-用户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for biz_flow_role
-- ----------------------------
DROP TABLE IF EXISTS `biz_flow_role`;
CREATE TABLE `biz_flow_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '数据ID',
  `biz_flow_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '业务流程ID',
  `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '审批角色组ID',
  `tenant_id` int NULL DEFAULT NULL COMMENT '租户ID',
  `product_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品ID',
  `biz_flow_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务流程名称',
  `del_flag` tinyint(1) NULL DEFAULT NULL COMMENT '逻辑删除标识(0-否，1-是)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `billType`(`biz_flow_id`) USING BTREE,
  INDEX `roleId`(`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 142 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for customize_component
-- ----------------------------
DROP TABLE IF EXISTS `customize_component`;
CREATE TABLE `customize_component`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '数据ID',
  `component_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据业务ID',
  `tenant_id` int NOT NULL COMMENT '租户ID',
  `product_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '产品ID',
  `workflow_type` tinyint(1) NOT NULL COMMENT '流程标识(1-审批流，2-工单流)',
  `module_type` tinyint(1) NULL DEFAULT NULL COMMENT '模块组件类型',
  `component_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件名称',
  `web_component` json NULL COMMENT '前端组件标识',
  `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除标识(0-否，1-是)',
  `creator_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户ID',
  `gmt_create` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `modifier_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人ID',
  `gmt_modified` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '前端组件展示比表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for customize_condition
-- ----------------------------
DROP TABLE IF EXISTS `customize_condition`;
CREATE TABLE `customize_condition`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '数据ID',
  `condition_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据业务ID',
  `tenant_id` int NOT NULL COMMENT '租户ID',
  `product_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品ID',
  `business_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务编码',
  `condition_name` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '条件名称(对应下拉选择的显示中文)',
  `condition_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '条件key(提交审批表单中的属性字段)',
  `condition_dictionary` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '条件对于的数据字典',
  `condition_dictionary_list` json NULL COMMENT '对应字典ID的JSON列表',
  `param_type` tinyint(1) NULL DEFAULT NULL COMMENT '条件类型(1-下拉列表，2-数字输入框，3-组织列表)',
  `del_flag` tinyint(1) NULL DEFAULT NULL COMMENT '逻辑删除标识(0-否，1-是)',
  `creator_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `modifier_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人ID',
  `gmt_modified` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '条件配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for customize_deploy
-- ----------------------------
DROP TABLE IF EXISTS `customize_deploy`;
CREATE TABLE `customize_deploy`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '数据ID',
  `deploy_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据业务ID',
  `tenant_id` int NOT NULL COMMENT '租户ID',
  `product_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品ID',
  `business_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务模块编码',
  `save_model_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务流程ID(对应业务动作为保存)',
  `save_def_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程定义ID',
  `save_version` tinyint NULL DEFAULT NULL COMMENT '流程版本',
  `save_node_data` json NULL COMMENT '前端流程节点JSON',
  `deploy_model_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务部署ID(对应业务系统部署)',
  `deploy_def_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程定义ID',
  `deploy_version` tinyint NULL DEFAULT NULL COMMENT '业务部署版本',
  `deploy_node_data` json NULL COMMENT '前端流程节点JSON',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `deploy_status` tinyint(1) NULL DEFAULT NULL COMMENT '部署状态',
  `is_enable` tinyint(1) NULL DEFAULT NULL COMMENT '启用状态',
  `del_flag` tinyint(1) NULL DEFAULT NULL COMMENT '逻辑删除标识(0-否，1-是)',
  `creator_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建人ID',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `modifier_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人ID',
  `gmt_modified` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程部署表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for customize_instance
-- ----------------------------
DROP TABLE IF EXISTS `customize_instance`;
CREATE TABLE `customize_instance`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '数据ID',
  `biz_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据业务ID',
  `tenant_id` int NOT NULL COMMENT '租户ID',
  `product_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品ID',
  `authority_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据权限ID',
  `business_code` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务模块编码',
  `def_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程定义ID',
  `instance_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程实例ID',
  `data_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '启动流程的业务数据ID',
  `put_audits_userid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提交审批用户ID',
  `put_audits_time` datetime NULL DEFAULT NULL COMMENT '提交审批时间',
  `task_id_list` json NULL COMMENT '当前任务ID的JSON数组',
  `flow_status` tinyint NULL DEFAULT NULL COMMENT '流程状态',
  `suspend_cause` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '流程挂起原因',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(1) NULL DEFAULT NULL COMMENT '逻辑删除标识(0-否，1-是)',
  `creator_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户ID',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `modifier_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `gmt_modified` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程实例表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for flow_and_view
-- ----------------------------
DROP TABLE IF EXISTS `flow_and_view`;
CREATE TABLE `flow_and_view`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT ' ',
  `def_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流程定义ID',
  `tenant_id` int NULL DEFAULT NULL COMMENT '租户ID',
  `product_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '产品ID',
  `view_node_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '前端节点ID',
  `flow_node_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'flow节点ID',
  `del_flag` tinyint(1) NULL DEFAULT NULL COMMENT '逻辑删除标识(0-否，1-是)',
  `biz_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务数据ID',
  `creator_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `gmt_create` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `modifier_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `gmt_modified` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_id`(`id`) USING BTREE,
  INDEX `index_def_id`(`def_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 994 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '前端节点和后端节点关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for task_user
-- ----------------------------
DROP TABLE IF EXISTS `task_user`;
CREATE TABLE `task_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '数据ID',
  `biz_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '业务数据ID',
  `tenant_id` int NULL DEFAULT NULL COMMENT '租户ID',
  `product_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产品ID',
  `instance_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程实例ID',
  `task_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程任务ID',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户ID',
  `flow_node_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程模板节点ID',
  `active` tinyint(1) NULL DEFAULT 1 COMMENT '活跃状态(1-未执行, 2-用户执行, 3-系统执行, 4-系统放弃[挂起等原因])',
  `del_flag` tinyint(1) NULL DEFAULT NULL COMMENT '逻辑删除标识(0-否，1-是)',
  `creator_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户ID',
  `gmt_create` datetime NOT NULL COMMENT '创建时间',
  `modifier_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '修改用户ID',
  `gmt_modified` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
