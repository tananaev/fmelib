# Fragments Made Easy

Android library that makes [Fragments](http://developer.android.com/guide/components/fragments.html) easy to work with.
* Really simple asynchronous calls management
* Stop worrying about configuration changes
* Create anounymous or inner-classes fragments

## Download
Just include Gradle dependency into your project:
```groovy
compile 'com.fmelib:fmelib:0.0.2'
```

## Callbacks and listeners

It was always a pain to handle various asynchronous callbacks is fragments because fragments can be re-created by Android. With retained fragments and magic `runWhenResumed(Task)` method, you don't need to worry about it any more.
```java
public class LoginFragment extends EasyFragment {
    ...
    public void login() {
        sendLoginRequest(new LoginRequestCallback() {
            @Override
            public void onSuccess(final String user) {
                runWhenResumed(new Task() {
                    @Override
                    public void run(boolean fragmentDestroyed) {
                        if (!fragmentDestroyed) {
                            startMainActivity(user);
    ...
```
Task will be executed immediately if fragment is active. If it's not currently active, the task will be executed when fragment is resumed. If fragment is destroyed, task will be executed with `fragmentDestroyed` flag allowing you to handle this situation.

Nice benefit of this approach is that you can cache response using standard Java closure which makes code shorter and simpler.

## Configuration changes

Every Android developer hates handling configurations changes. With retained fragments you don't need to worry about it. All necessary data can be stored directly in the fragment class and it's persisted between configuration changes.

Awesome `runWhenResumed(Task)` helps here as well. If you are using it for all listeners and callbacks, you can be sure that nothing would be called during configuration change. Calls to `getView()` will always return valid active view of the fragment attached to the activity layout.

## Anonymous fragments

Why do you need so much code to create a simple fragment? Google says that it should be static or outer class with default public constructor. Forget all those annoying rules and make your new fragment inline with just a few lines of code.
```java
final String user = getIntent().getStringExtra(USER_KEY);

findViewById(R.id.welcome_button).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        DialogFragment dialog = new EasyDialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                return new AlertDialog.Builder(getActivity())
                        .setMessage(String.format(getString(R.string.message), user))
                        .setPositiveButton(android.R.string.ok, null)
                        .create();
            }
        };
```
How easy is that? Access methods and variables from outer class or method using standard Java code. Make your code shorted, simpler and easier to read.

## Contacts

Author - Anton Tananaev ([anton.tananaev@gmail.com](mailto:anton.tananaev@gmail.com))

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
