# Git协作开发指南

## 项目概述
微信小程序商城项目，包含Java后端API、Vue管理后台和微信小程序前端。

## 1. Git环境准备

### 1.1 Git安装验证
项目已检测到Git版本：`git version 2.51.0.windows.1`

### 1.2 Git基本配置
首次使用Git需要配置用户信息：
```bash
# 配置用户名和邮箱
git config --global user.name "你的姓名"
git config --global user.email "你的邮箱@example.com"

# 配置默认分支名称
git config --global init.defaultBranch main

# 配置换行符处理（Windows推荐）
git config --global core.autocrlf true

# 配置编辑器（可选）
git config --global core.editor "code --wait"  # 使用VS Code
```

## 2. 仓库结构和分支策略

### 2.1 分支管理策略
采用Git Flow工作流：

```
main                    # 主分支，生产环境代码
├── develop            # 开发分支，集成最新功能
├── feature/           # 功能分支
│   ├── feature/payment    # 支付功能
│   ├── feature/order      # 订单功能
│   └── feature/user       # 用户功能
├── release/           # 发布分支
│   └── release/v1.0.0     # 版本发布
└── hotfix/            # 热修复分支
    └── hotfix/bug-fix     # 紧急修复
```

### 2.2 分支命名规范
- **功能分支**: `feature/功能名称`
- **修复分支**: `bugfix/问题描述`
- **热修复分支**: `hotfix/紧急修复`
- **发布分支**: `release/版本号`

## 3. 基本操作流程

### 3.1 克隆和初始设置
```bash
# 克隆远程仓库
git clone https://github.com/username/wechat-mall.git
cd wechat-mall

# 或者关联现有项目到远程仓库
git remote add origin https://github.com/username/wechat-mall.git
```

### 3.2 日常开发流程

#### 开始新功能开发
```bash
# 切换到develop分支并更新
git checkout develop
git pull origin develop

# 创建功能分支
git checkout -b feature/new-feature

# 开发完成后提交
git add .
git commit -m "feat: 添加新功能"

# 推送到远程
git push origin feature/new-feature
```

#### 合并功能分支
```bash
# 切换到develop分支
git checkout develop
git pull origin develop

# 合并功能分支
git merge feature/new-feature

# 推送更新
git push origin develop

# 删除已合并的功能分支
git branch -d feature/new-feature
git push origin --delete feature/new-feature
```

### 3.3 发布流程
```bash
# 从develop创建发布分支
git checkout develop
git pull origin develop
git checkout -b release/v1.0.0

# 发布准备（版本号更新、文档等）
git add .
git commit -m "chore: 准备v1.0.0发布"

# 合并到main分支
git checkout main
git merge release/v1.0.0
git tag -a v1.0.0 -m "发布版本v1.0.0"

# 合并回develop分支
git checkout develop
git merge release/v1.0.0

# 推送所有更新
git push origin main
git push origin develop
git push origin v1.0.0

# 删除发布分支
git branch -d release/v1.0.0
```

## 4. 提交规范

### 4.1 提交信息格式
使用约定式提交（Conventional Commits）：
```
<类型>[可选的作用域]: <描述>

[可选的正文]

[可选的脚注]
```

### 4.2 提交类型
- **feat**: 新功能
- **fix**: 修复bug
- **docs**: 文档更新
- **style**: 代码格式调整
- **refactor**: 代码重构
- **test**: 测试相关
- **chore**: 构建过程或辅助工具的变动

### 4.3 提交示例
```bash
# 新功能
git commit -m "feat(backend): 添加商品查询接口"

# 修复bug
git commit -m "fix(frontend): 修复购物车数量显示问题"

# 文档更新
git commit -m "docs: 更新API文档"

# 代码重构
git commit -m "refactor(service): 优化订单服务逻辑"
```

## 5. 团队协作规范

### 5.1 代码审查流程
1. **创建Pull Request**: 功能开发完成后创建PR
2. **代码审查**: 至少一人审查代码
3. **测试验证**: 确保功能正常运行
4. **合并代码**: 审查通过后合并到目标分支

### 5.2 冲突解决
```bash
# 拉取最新代码
git pull origin develop

# 如果有冲突，手动解决冲突文件
# 编辑冲突文件，删除冲突标记

# 标记冲突已解决
git add 冲突文件名

# 完成合并
git commit -m "resolve: 解决合并冲突"
```

### 5.3 协作最佳实践
- **频繁提交**: 小步快跑，避免大批量提交
- **及时同步**: 每天开始工作前先拉取最新代码
- **分支隔离**: 不同功能使用不同分支开发
- **代码审查**: 重要功能必须经过代码审查

## 6. 项目特定配置

### 6.1 Java后端开发
```bash
# 后端开发流程
cd backend-java/wechat-mall-api

# 确保Maven构建成功
mvn clean compile

# 运行测试
mvn test

# 提交前检查
git add .
git commit -m "feat(backend): 完善商品服务接口"
```

### 6.2 Vue前端开发
```bash
# 前端开发流程
cd frontend/merchant-admin

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建检查
npm run build

# 提交代码
git add .
git commit -m "feat(frontend): 添加商品管理页面"
```

### 6.3 微信小程序开发
```bash
# 小程序开发注意事项
# 1. 不要提交 project.private.config.json
# 2. 确保在微信开发者工具中测试通过
# 3. 注意小程序的文件大小限制

git add pages/ utils/ app.js app.json app.wxss
git commit -m "feat(miniprogram): 添加商品详情页面"
```

## 7. 常用Git命令参考

### 7.1 基本命令
```bash
git status                  # 查看状态
git add .                   # 添加所有文件
git add 文件名              # 添加指定文件
git commit -m "提交信息"    # 提交代码
git push                    # 推送到远程
git pull                    # 拉取远程更新
```

### 7.2 分支操作
```bash
git branch                  # 查看本地分支
git branch -r              # 查看远程分支
git branch -a              # 查看所有分支
git checkout 分支名        # 切换分支
git checkout -b 新分支名   # 创建并切换分支
git merge 分支名           # 合并分支
git branch -d 分支名       # 删除分支
```

### 7.3 历史查看
```bash
git log                     # 查看提交历史
git log --oneline          # 简洁历史
git log --graph            # 图形化历史
git show 提交ID            # 查看具体提交
git diff                   # 查看工作区差异
git diff --cached          # 查看暂存区差异
```

### 7.4 撤销操作
```bash
git checkout -- 文件名     # 撤销工作区修改
git reset HEAD 文件名      # 撤销暂存区修改
git reset --soft HEAD~1    # 撤销最后一次提交（保留修改）
git reset --hard HEAD~1    # 撤销最后一次提交（丢弃修改）
```

## 8. 远程仓库配置

### 8.1 常用Git托管平台
- **GitHub**: https://github.com
- **GitLab**: https://gitlab.com
- **Gitee**: https://gitee.com
- **Azure DevOps**: https://dev.azure.com

### 8.2 SSH密钥配置
```bash
# 生成SSH密钥
ssh-keygen -t rsa -b 4096 -C "your_email@example.com"

# 添加到ssh-agent
ssh-add ~/.ssh/id_rsa

# 复制公钥到剪贴板（Windows）
clip < ~/.ssh/id_rsa.pub

# 在Git平台添加SSH公钥
```

## 9. 故障排除

### 9.1 常见问题
- **推送被拒绝**: 先拉取远程更新 `git pull`
- **合并冲突**: 手动编辑冲突文件，删除冲突标记
- **误提交敏感信息**: 使用 `git filter-branch` 或 BFG 工具清理
- **分支混乱**: 使用 `git reflog` 查看操作历史

### 9.2 最佳实践
- 定期备份重要分支
- 使用 `.gitignore` 忽略不必要的文件
- 保持提交历史清晰
- 及时删除已合并的分支

## 10. 工具推荐

### 10.1 Git GUI工具
- **SourceTree**: 免费的Git图形界面
- **GitKraken**: 功能强大的Git客户端
- **VS Code**: 内置Git支持
- **GitHub Desktop**: GitHub官方客户端

### 10.2 VS Code插件
- **GitLens**: 增强Git功能
- **Git Graph**: 可视化Git历史
- **Git History**: 查看文件历史

---
*本指南会根据项目需要持续更新*