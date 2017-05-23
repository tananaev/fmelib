# Fragments Made Easy

Android library that makes [Fragments](http://developer.android.com/guide/components/fragments.html) easy to work with.
- Really simple asynchronous calls management
- Stop worrying about saving fragment state

Current version of the library only provides classes based on Android Support Library Fragment, so you have to include Support Library dependency if you want to use `EasyFragment`.

## Download
Just include Gradle dependency into your project:
```groovy
compile 'com.tananaev:fmelib:1.0'
```

## Callbacks and listeners

It was always a pain to handle various asynchronous callbacks is fragments because fragments can be recreated by Android. With `EasyFragment` and magic `runWhenStarted(Task)` method you don't need to worry about it any more.

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

You can use anonymous clasess or lambda expressions for implementing `Task` interface. Library will automatically serialize your callback instance and associate it with a new fragment if your activity has been recreated. The only thing that you need to make sure that all extra variables captured by the class or lambda are `Serializable`.

Nice benefit of this approach is that you can cache response using standard Java closure which makes code shorter and simpler.

## Fragment recreation

Every Android developer knows the struggle of dealing with configuration changes. Some developers try to avoid it by using retained fragments or disabling activity recreation on certain configuration change events (e.g. orientation change), but the problem is that Android system can't guarantee that your activity will be kept alive. Even if it's not recreated on configuration change, it can be destroyed and recreated when Android is low on memory or some other exception circumstances.

The library makes it easy to handle fragment recreation by automatically saving and restoring member variables that. All you need to do is mark fields that you want to save with `@EasySaveInstance` annotation. Any primitive types, Serializable and Parcelable variables are supported.

```java
public class MainFragment extends EasyFragment {

    @EasySaveInstance
    private ArrayList<String> array;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        if (array == null) array = loadData(); // array is saved and recreated automatically

        view.findViewById(android.R.id.list).setAdapter(
            new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, array));

        return view;
    }

    ...
}
```

Awesome `runWhenStarted(Task)` method helps with managing recreation as well. If you are using it for all listeners and callbacks, you can be sure that nothing would be called during configuration change. Calls to `getView()` will always return valid and visible view of the fragment attached to the activity layout.

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
