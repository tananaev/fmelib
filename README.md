# Fragments Made Easy

Android library that makes [Fragments](http://developer.android.com/guide/components/fragments.html) easy to work with.

- Easy asynchronous callbacks management

Main goal of the library is to allow developers focus more about business logic they are implementing and think less about proper handling of fragment lifecycle.

Current version of the library only provides classes based on Android Support Library Fragment, so you have to include Support Library dependency if you want to use `EasyFragment`.

## Download
Just include Gradle dependency into your project:
```groovy
compile "com.tananaev:fmelib:${version}"
```

## Callbacks and listeners

It was always a pain to handle various asynchronous callbacks is fragments because fragments can be recreated by Android. With `EasyFragment` and magic `runWhenStarted(Task)` method you don't need to worry about it anymore.

```java
public class LoginFragment extends EasyFragment {
    ...
    public void login() {
        sendLoginRequest(user -> runWhenStarted(fragmentDestroyed -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(MainActivity.USER_KEY, user);
            startActivity(intent);
        }));
    }
    ...
```

Task will be executed immediately if fragment is in started state. If it's not currently visible, the task will be executed when fragment is started. If fragment is destroyed, task will be executed with `fragmentDestroyed` flag allowing you to handle this condition as well.

You can use anonymous clasess or lambda expressions for implementing `Task` interface. Nice benefit of this approach is that you can cache response using standard Java closure which makes code shorter and simpler. Library will automatically serialize your callback instance and associate it with a new fragment if your activity has been recreated.

Just make sure that all extra variables captured by your `Task` class or lambda are `Serializable`.

## Team

- [Anton Tananaev](https://github.com/tananaev)
- [Nader Ayyad](https://github.com/naderz)

## License

    Apache License, Version 2.0

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
