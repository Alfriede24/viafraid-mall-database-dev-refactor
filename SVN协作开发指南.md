# SVN协作开发指南

## 项目概述
微信小程序商城项目，包含Java后端API、Vue管理后台和微信小程序前端。

## 1. SVN环境准备

### 1.1 安装SVN客户端
推荐使用以下SVN客户端工具：

**Windows系统：**
- **TortoiseSVN**（推荐）：图形化界面，易于使用
  - 下载地址：https://tortoisesvn.net/downloads.html
  - 安装后重启计算机
- **命令行SVN**：适合高级用户
  - 可通过Chocolatey安装：`choco install svn`

**macOS系统：**
- **Cornerstone**：专业的SVN客户端
- **命令行SVN**：通过Homebrew安装：`brew install svn`

### 1.2 验证安装
安装完成后，在命令行中运行：
```bash
svn --version
```

## 2. SVN仓库结构

### 2.1 标准目录结构
```
wechat-mall-svn/
├── trunk/                 # 主开发分支
│   ├── backend-java/      # Java后端代码
│   ├── frontend/          # Vue管理后台
│   ├── pages/             # 微信小程序页面
│   ├── utils/             # 工具类
│   └── ...
├── branches/              # 功能分支
│   ├── feature-payment/   # 支付功能分支
│   ├── feature-order/     # 订单功能分支
│   └── ...
└── tags/                  # 版本标签
    ├── v1.0.0/           # 版本1.0.0
    ├── v1.1.0/           # 版本1.1.0
    └── ...
```

### 2.2 分支管理策略
- **trunk**：主开发分支，保持稳定可运行状态
- **branches**：功能开发分支，用于新功能开发
- **tags**：版本发布标签，不可修改

## 3. 基本操作流程

### 3.1 初次检出项目
```bash
# 检出主分支
svn checkout http://your-svn-server/wechat-mall/trunk wechat-mall

# 进入项目目录
cd wechat-mall
```

### 3.2 日常开发流程

#### 开始工作前
```bash
# 更新到最新版本
svn update

# 查看状态
svn status
```

#### 提交代码
```bash
# 添加新文件（如果有）
svn add filename

# 查看修改内容
svn diff

# 提交代码
svn commit -m "详细的提交说明"
```

### 3.3 分支操作

#### 创建功能分支
```bash
# 从trunk创建分支
svn copy http://your-svn-server/wechat-mall/trunk \
         http://your-svn-server/wechat-mall/branches/feature-name \
         -m "创建功能分支：功能描述"

# 检出分支
svn checkout http://your-svn-server/wechat-mall/branches/feature-name
```

#### 合并分支
```bash
# 在trunk目录下执行
svn merge http://your-svn-server/wechat-mall/branches/feature-name

# 解决冲突后提交
svn commit -m "合并功能分支：功能描述"
```

## 4. 团队协作规范

### 4.1 提交规范
- **提交频率**：每完成一个小功能就提交，避免大批量提交
- **提交信息**：使用中文，格式如下：
  ```
  [模块] 功能描述
  
  详细说明：
  - 修改内容1
  - 修改内容2
  ```

### 4.2 提交信息示例
```
[后端] 完善商品查询接口注释

详细说明：
- 完善ProductController中查询方法的JavaDoc注释
- 添加详细的参数说明和返回值描述
- 统一注释格式和规范
```

### 4.3 冲突解决
1. **更新代码**：`svn update`
2. **查看冲突**：查找包含`<<<<<<<`、`=======`、`>>>>>>>`的文件
3. **手动解决**：编辑冲突文件，保留正确的代码
4. **标记解决**：`svn resolved filename`
5. **提交代码**：`svn commit`

### 4.4 文件管理
- **忽略文件**：项目已配置`.svnignore`文件
- **二进制文件**：图片、文档等需要设置正确的MIME类型
- **删除文件**：使用`svn delete`而不是直接删除

## 5. 项目特定配置

### 5.1 Java后端
- 忽略`target/`目录和IDE配置文件
- 提交前确保代码编译通过
- 数据库配置文件不要提交敏感信息

### 5.2 Vue前端
- 忽略`node_modules/`和`dist/`目录
- 提交前运行`npm run build`确保构建成功
- 环境配置文件使用模板形式

### 5.3 微信小程序
- 忽略`project.private.config.json`
- 提交前在微信开发者工具中测试

## 6. 常用命令参考

### 6.1 基本命令
```bash
svn help                    # 查看帮助
svn info                    # 查看仓库信息
svn status                  # 查看文件状态
svn update                  # 更新代码
svn commit -m "message"     # 提交代码
svn add filename            # 添加文件
svn delete filename         # 删除文件
svn move old new           # 移动/重命名文件
```

### 6.2 查看历史
```bash
svn log                     # 查看提交历史
svn log -l 10              # 查看最近10次提交
svn diff                   # 查看本地修改
svn diff -r 100:200        # 查看版本间差异
```

### 6.3 版本管理
```bash
svn copy trunk tags/v1.0.0 # 创建版本标签
svn switch URL             # 切换分支
svn merge -r 100:200 URL   # 合并特定版本
```

## 7. 故障排除

### 7.1 常见问题
- **工作副本损坏**：删除`.svn`目录，重新检出
- **文件锁定**：`svn cleanup`清理工作副本
- **权限问题**：检查SVN服务器权限配置

### 7.2 最佳实践
- 定期备份SVN仓库
- 使用钩子脚本进行代码质量检查
- 建立代码审查流程
- 定期清理无用分支

## 8. 联系方式
如有问题，请联系项目负责人或查阅SVN官方文档。

---
*本指南会根据项目需要持续更新*