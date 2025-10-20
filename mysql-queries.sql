-- MySQL数据库查询脚本
-- 数据库：wechat_mall
-- 连接信息：localhost:3306, 用户：root, 密码：pvah1376

-- 使用数据库
USE wechat_mall;

-- ========================================
-- 1. 查看数据库中的所有表
-- ========================================
SHOW TABLES;

-- ========================================
-- 2. 查看表结构
-- ========================================

-- 查看用户表结构
DESCRIBE Users;
SHOW CREATE TABLE Users;

-- 查看商品表结构
DESCRIBE Products;
SHOW CREATE TABLE Products;

-- 查看订单表结构
DESCRIBE Orders;
SHOW CREATE TABLE Orders;

-- 查看分类表结构
DESCRIBE Categories;
SHOW CREATE TABLE Categories;

-- ========================================
-- 3. 基础数据查询
-- ========================================

-- 查询所有用户
SELECT * FROM Users;

-- 查询所有商品分类
SELECT * FROM Categories;

-- 查询所有商品（包含分类信息）
SELECT 
    p.Id,
    p.Name,
    p.Description,
    p.Price,
    p.Stock,
    p.ImageUrl,
    c.Name AS CategoryName
FROM Products p
LEFT JOIN Categories c ON p.CategoryId = c.Id;

-- 查询所有订单（包含用户信息）
SELECT 
    o.Id,
    o.OrderNumber,
    o.TotalAmount,
    o.Status,
    o.CreatedAt,
    u.Username,
    u.Phone
FROM Orders o
LEFT JOIN Users u ON o.UserId = u.Id;

-- 查询所有商家
SELECT * FROM Merchants;

-- 查询所有门店（包含商家信息）
SELECT 
    s.Id,
    s.Name,
    s.Address,
    s.Phone,
    s.IsActive,
    m.Name AS MerchantName
FROM Stores s
LEFT JOIN Merchants m ON s.MerchantId = m.Id;

-- ========================================
-- 4. 统计查询
-- ========================================

-- 数据库统计信息
SELECT 
    '用户总数' AS 统计项,
    COUNT(*) AS 数量
FROM Users
UNION ALL
SELECT 
    '商品总数' AS 统计项,
    COUNT(*) AS 数量
FROM Products
UNION ALL
SELECT 
    '订单总数' AS 统计项,
    COUNT(*) AS 数量
FROM Orders
UNION ALL
SELECT 
    '分类总数' AS 统计项,
    COUNT(*) AS 数量
FROM Categories
UNION ALL
SELECT 
    '商家总数' AS 统计项,
    COUNT(*) AS 数量
FROM Merchants
UNION ALL
SELECT 
    '门店总数' AS 统计项,
    COUNT(*) AS 数量
FROM Stores;

-- 按分类统计商品数量
SELECT 
    c.Name AS 分类名称,
    COUNT(p.Id) AS 商品数量
FROM Categories c
LEFT JOIN Products p ON c.Id = p.CategoryId
GROUP BY c.Id, c.Name
ORDER BY 商品数量 DESC;

-- 按状态统计订单数量
SELECT 
    Status AS 订单状态,
    COUNT(*) AS 订单数量
FROM Orders
GROUP BY Status
ORDER BY 订单数量 DESC;

-- ========================================
-- 5. 复杂查询示例
-- ========================================

-- 查询订单详情（包含订单项和商品信息）
SELECT 
    o.OrderNumber AS 订单号,
    u.Username AS 用户名,
    oi.ProductName AS 商品名称,
    oi.Quantity AS 数量,
    oi.Price AS 单价,
    (oi.Quantity * oi.Price) AS 小计,
    o.TotalAmount AS 订单总额,
    o.Status AS 订单状态,
    o.CreatedAt AS 下单时间
FROM Orders o
LEFT JOIN Users u ON o.UserId = u.Id
LEFT JOIN OrderItems oi ON o.Id = oi.OrderId
ORDER BY o.CreatedAt DESC
LIMIT 20;

-- 查询用户购物车
SELECT 
    u.Username AS 用户名,
    p.Name AS 商品名称,
    ci.Quantity AS 数量,
    p.Price AS 单价,
    (ci.Quantity * p.Price) AS 小计,
    ci.CreatedAt AS 加入时间
FROM CartItems ci
LEFT JOIN Users u ON ci.UserId = u.Id
LEFT JOIN Products p ON ci.ProductId = p.Id
ORDER BY ci.CreatedAt DESC;

-- 查询用户地址
SELECT 
    u.Username AS 用户名,
    ua.ReceiverName AS 收货人,
    ua.Phone AS 电话,
    ua.Province AS 省份,
    ua.City AS 城市,
    ua.District AS 区县,
    ua.DetailAddress AS 详细地址,
    ua.IsDefault AS 是否默认
FROM UserAddresses ua
LEFT JOIN Users u ON ua.UserId = u.Id
ORDER BY ua.IsDefault DESC, ua.CreatedAt DESC;

-- 查询优惠券使用情况
SELECT 
    ct.Name AS 优惠券名称,
    ct.DiscountAmount AS 优惠金额,
    ct.MinOrderAmount AS 最低消费,
    COUNT(uc.Id) AS 领取数量,
    COUNT(cur.Id) AS 使用数量
FROM CouponTemplates ct
LEFT JOIN UserCoupons uc ON ct.Id = uc.CouponTemplateId
LEFT JOIN CouponUsageRecords cur ON uc.Id = cur.UserCouponId
GROUP BY ct.Id, ct.Name, ct.DiscountAmount, ct.MinOrderAmount
ORDER BY 领取数量 DESC;

-- 查询物流信息
SELECT 
    o.OrderNumber AS 订单号,
    li.TrackingNumber AS 快递单号,
    li.ExpressCompany AS 快递公司,
    li.Status AS 物流状态,
    li.CurrentLocation AS 当前位置,
    li.ShippedAt AS 发货时间,
    li.DeliveredAt AS 签收时间,
    li.ReceiverName AS 收货人,
    li.ReceiverPhone AS 收货电话
FROM LogisticsInfos li
LEFT JOIN Orders o ON li.OrderId = o.Id
ORDER BY li.UpdatedAt DESC;

-- ========================================
-- 6. 索引和性能查询
-- ========================================

-- 查看表的索引信息
SHOW INDEX FROM Users;
SHOW INDEX FROM Products;
SHOW INDEX FROM Orders;

-- 查看表的存储引擎和字符集
SELECT 
    TABLE_NAME AS 表名,
    ENGINE AS 存储引擎,
    TABLE_COLLATION AS 字符集,
    TABLE_ROWS AS 行数,
    DATA_LENGTH AS 数据大小,
    INDEX_LENGTH AS 索引大小
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'wechat_mall'
ORDER BY TABLE_NAME;

-- ========================================
-- 7. 数据完整性检查
-- ========================================

-- 检查外键约束
SELECT 
    CONSTRAINT_NAME AS 约束名,
    TABLE_NAME AS 表名,
    COLUMN_NAME AS 列名,
    REFERENCED_TABLE_NAME AS 引用表,
    REFERENCED_COLUMN_NAME AS 引用列
FROM information_schema.KEY_COLUMN_USAGE 
WHERE TABLE_SCHEMA = 'wechat_mall' 
AND REFERENCED_TABLE_NAME IS NOT NULL;

-- 检查数据一致性
-- 检查是否有无效的分类ID
SELECT COUNT(*) AS 无效分类商品数量
FROM Products p
LEFT JOIN Categories c ON p.CategoryId = c.Id
WHERE c.Id IS NULL;

-- 检查是否有无效的用户ID订单
SELECT COUNT(*) AS 无效用户订单数量
FROM Orders o
LEFT JOIN Users u ON o.UserId = u.Id
WHERE u.Id IS NULL;

-- ========================================
-- 使用说明
-- ========================================
/*
连接MySQL数据库的方法：

1. 使用命令行工具：
   mysql -h localhost -P 3306 -u root -ppvah1376 -D wechat_mall

2. 使用图形化工具（如MySQL Workbench、phpMyAdmin等）：
   主机：localhost
   端口：3306
   用户名：root
   密码：pvah1376
   数据库：wechat_mall

3. 使用Web API接口：
   访问 http://localhost:5277/database-query.html
   
注意事项：
- 所有表都使用InnoDB存储引擎
- 字符集为utf8mb4，支持emoji等特殊字符
- 已设置适当的外键约束保证数据完整性
- 建议在生产环境中设置更复杂的数据库密码
*/