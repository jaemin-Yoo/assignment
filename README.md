# Development Environment

```
IDE: Android Studio Hedgehog
OS: Microsoft Windows 11 Pro
```

# Project Setting

```properties
// local.properties

...
UNSPLASH_ACCESS_KEY="YOUR_ACCESS_KEY"
```

**Unsplash API**를 사용하기 위해 `Access key`를 등록해야 합니다.

# Features

### 🔍 피드 화면 이미지 검색

<img src="/docs/images/search_images.gif" width="320"/>

### ⭐ 이미지 즐겨찾기

<img src="/docs/images/favorite_images.gif" width="320"/>

# Tech Stack

### Architecture

- MVVM
- Repository

### Jetpack

- Compose
- Hilt
- Navigation
- ViewModel
- Paging
- Preferences DataStore

### Kotlin

- Coroutines
- Flow
- Serialization

### Etc

- Retrofit2
- Okhttp3
- Glide
- Min SDK 24

# Trouble Shooting

### 즐겨찾기 이미지 저장 방식

이미지를 즐겨찾기를 할 때 데이터를 저장하기 위해서 어떤 라이브러리를 사용할 지 고민하였습니다. 일반적인 즐겨찾기 기능은 앱이 종료되어도 데이터가 남아있기 때문에, 데이터를 영구적으로 저장할 수 있는 라이브러리인 Room, DataStore, SharedPreferences 중 하나를 선택하기로 결정하였습니다.

이미지에 사용되는 정보가 `id`와 `url` 뿐이었기에, 데이터 구조가 복잡하지 않아 Room을 제외했고 데이터 무결성, 스레드 안전성, 비동기 처리를 제공하는 DataStore를 선택하였습니다. 구조화된 데이터를 저장할 수 있는 Proto DataStore를 선택하지 않고 `id`와 `url`을 문자열 형태로 Preferences DataStore에 값으로 저장하는 방식을 선택하였습니다.

### 네트워크 응답 상태에 따른 UI 변경

이미지 검색 전, 검색 중, 검색 후, 네트워크 오류 발생, 검색 결과가 없을 때 등 이미지를 가져올 때 다양한 상태가 존재합니다. 이러한 상태들에 대한 UI를 표현하기 위해 각 화면에 대한 `UiState`를 적용하였습니다. `sealed interface`로 UiState를 정의하여 UI에서 상태에 따라 다른 화면을 보여주었습니다.
