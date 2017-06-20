# ChiBa

## Android Requirements
The app is developed for a specific android environment. To avoid conflicts we set up the following requirements:
- Samsung Galaxy S7 Edge
- 5,5 " screen size
- resolution of 1440 x 2560 px
- 4 GB RAM

## OpenGL Requirements
- OpenGL ES 2.0 API
- data format: OBJ 
- calculation of normals
- transformations: scaling, rotation, translation
- colors
- textures
- lightning

## Version control

### Commit messages
Commit messages are inspired by the [AngularJS commit message guidelines](https://docs.google.com/document/d/1QrDFcIiPjSLDn3EL15IJygNPiHORgU1_OOAqWjiDU5Y/edit).
This enables automatic generation of the changelog from commit messages.

### Commit message format
Each commit message consists of a **featureId**, a **type**, a **scope** and a **subject**:

```
[<#featureId] <type> (<scope>) -> <subject>
```

#### Feature ID

If the Feature ID is unknown or not defined, use [#00].

#### Type

Must be one of the following:

*   **feat:** A new feature
*   **fix:** A bug fix
*   **refactor:** A code change that neither fixes a bug nor adds a feature
*   **perf:** A code change that improves performance
*   **style:** Changes that do not affect the meaning of the code (white-space, formatting, missing semi-colons, etc)
*   **docs:** Documentation only changes
*   **test:** Adding missing tests
*   **chore:** Changes to the build process or auxiliary tools and libraries such as documentation generation

#### Scope

The scope could be anything specifying place of the commit change. For example $location, $browser, $compile, $rootScope, ngHref, ngClick, ngView, etc...
You can use * if there isn't a more fitting scope.

#### Subject

The subject contains short description (<50 characters) of the change:
* use the imperative, present tense: **_"change"_** not *_"changed"_* nor *_"changes"_*
* do not capitalize first letter
* no dot (.) at the end

**A properly formed git commit subject line should always be able to complete the following sentence:**
* If applied, this commit will **(your subject line here)**
* If applied, this commit will **refactor subsystem X for readability**
* If applied, this commit will **update getting started documentation**
* If applied, this commit will **remove deprecated methods**
* If applied, this commit will **release version 1.0.0**
* If applied, this commit will **merge pull request #123 from user/branch**

### Examples

```
[#01]fix(graphite): stop graphite breaking when width < 0.1

[#00]feat(readme): add new conventions
```

### Branching

* master is a protected branch 
* feature branches are branched of master and should fullfill a feature
* the feature branches are eventually merged back to master

### Merging

Feature branches are explicitly merged back using pull requests for code review. A local history clean-up rebase before sharing a feature branch for review is absolutely encouraged.

**How to rebase your feature branch with master:**

```
git checkout master         //go to master
git pull                    //get the latest changes on master
git checkout feature        //go back to feature branch
git rebase master           //rebase feature with master
git checkout master         //go back to master
git merge feature           //conflict-free merge of master with feature
git push                    //push changes to servers
```

If there are rebase conflicts, resolve them. You have to add the resolved files - see it with __git status__. Finally continue rebasing with:

```
git rebase --continue
```

In general: **Never work directly on public branche master, to avoid errors!**
