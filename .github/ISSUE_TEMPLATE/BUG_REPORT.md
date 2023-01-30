---
name: Bug report
about: Report a bug with this project
title: 'bug:'
labels: bug
assignees: nullptr-rs

---

### 🐛 What's the bug ?
The Pair class io.github.nullptr.tools.types.Pair is not working properly, I can't serialize it without problems.

### 🔍 In which context, with which version of the library, and with which code did you encounter the problem ?
This issue appears in the release 1.2.0 of the library, with this code:
```java
final String serialized = new Gson().toJson(new Pair<>(1, 2));
```

### 📸 Additional details / screenshots