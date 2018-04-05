/**
 * Created by Patrick O'Connor on 8/30/17.
 * https://github.com/RobertSheaO/react-native-zendesk-support
 */

package com.robertsheao.RNZenDeskSupport;

import android.content.Intent;
import android.app.Activity;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.zendesk.sdk.feedback.ui.ContactZendeskActivity;
import com.zendesk.sdk.requests.RequestActivity;
import com.zendesk.sdk.support.SupportActivity;
import com.zendesk.sdk.support.ContactUsButtonVisibility;
import com.zendesk.sdk.model.access.AnonymousIdentity;
import com.zendesk.sdk.model.access.Identity;
import com.zendesk.sdk.model.request.CustomField;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zendesk.sdk.feedback.BaseZendeskFeedbackConfiguration;
import com.zendesk.sdk.feedback.ZendeskFeedbackConfiguration;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import java.io.Serializable;

class FeedbackConfig extends BaseZendeskFeedbackConfiguration implements Serializable {

    String subject = null;
    List<String> tags = null;

    public FeedbackConfig(String subject, List<String> tags){
      super();
      this.subject = subject;
      this.tags = tags;
    }

    @Override
    public String getRequestSubject() {
      return this.subject;
    }

    @Override
    public List<String> getTags() {
      return this.tags;
    }
  }

public class RNZenDeskSupportModule extends ReactContextBaseJavaModule {

  private static final int REQUEST_CODE = 304869;

  public RNZenDeskSupportModule(final ReactApplicationContext reactContext) {
    super(reactContext);
    reactContext.addActivityEventListener(new BaseActivityEventListener() {
      @Override
      public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE) {
          if (resultCode == Activity.RESULT_CANCELED) {
            reactContext
              .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
              .emit("submitRequestCancelled", null);
          } else if (resultCode == Activity.RESULT_OK) {
            reactContext
              .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
              .emit("submitRequestCompleted", null);
          }
        }
      }
    });
  }

  @Override
  public String getName() {
    return "RNZenDeskSupport";
  }

  private static long[] toLongArray(ArrayList<?> values) {
    long[] arr = new long[values.size()];
    for (int i = 0; i < values.size(); i++)
      arr[i] = Long.parseLong((String) values.get(i));
    return arr;
  }

  @ReactMethod
  public void initialize(ReadableMap config) {
    String appId = config.getString("appId");
    String zendeskUrl = config.getString("zendeskUrl");
    String clientId = config.getString("clientId");
    ZendeskConfig.INSTANCE.init(getReactApplicationContext(), zendeskUrl, appId, clientId);
  }

  @ReactMethod
    public void setupIdentity(ReadableMap identity) {
      AnonymousIdentity.Builder builder = new AnonymousIdentity.Builder();

      if (identity != null && identity.hasKey("customerEmail")) {
        builder.withEmailIdentifier(identity.getString("customerEmail"));
      }

      if (identity != null && identity.hasKey("customerName")) {
        builder.withNameIdentifier(identity.getString("customerName"));
      }

      ZendeskConfig.INSTANCE.setIdentity(builder.build());
    }

  @ReactMethod
  public void showHelpCenterWithOptions(ReadableMap options) {
    SupportActivityBuilder.create()
      .withOptions(options)
      .show(getReactApplicationContext());
  }

  @ReactMethod
  public void showCategoriesWithOptions(ReadableArray categoryIds, ReadableMap options) {
    SupportActivityBuilder.create()
      .withOptions(options)
      .withArticlesForCategoryIds(categoryIds)
      .show(getReactApplicationContext());
  }

  @ReactMethod
  public void showSectionsWithOptions(ReadableArray sectionIds, ReadableMap options) {
    SupportActivityBuilder.create()
      .withOptions(options)
      .withArticlesForSectionIds(sectionIds)
      .show(getReactApplicationContext());
  }

  @ReactMethod
  public void showLabelsWithOptions(ReadableArray labels, ReadableMap options) {
    SupportActivityBuilder.create()
      .withOptions(options)
      .withLabelNames(labels)
      .show(getReactApplicationContext());
  }

  @ReactMethod
  public void showHelpCenter() {
    showHelpCenterWithOptions(null);
  }

  @ReactMethod
  public void showCategories(ReadableArray categoryIds) {
    showCategoriesWithOptions(categoryIds, null);
  }

  @ReactMethod
  public void showSections(ReadableArray sectionIds) {
    showSectionsWithOptions(sectionIds, null);
  }

  @ReactMethod
  public void showLabels(ReadableArray labels) {
    showLabelsWithOptions(labels, null);
  }

  @ReactMethod
  public void callSupport(final ReadableMap options) {

    String subject = null;
    List<String> tags = null;

    if(options.hasKey("subject"))
      subject = options.getString("subject");

    if(options.hasKey("tags")){
      ReadableArray tagsArray = options.getArray("tags");
      tags = new ArrayList(tagsArray.size());
      for(int i = 0; i < tagsArray.size(); i++){
        tags.add(tagsArray.getString(i));
      }
    }

    ZendeskFeedbackConfiguration configuration = new FeedbackConfig(subject, tags);

    if(options.hasKey("customFields")){
      List<CustomField> fields = new ArrayList<>();

      for (Map.Entry<String, Object> next : options.getMap("customFields").toHashMap().entrySet())
        fields.add(new CustomField(Long.parseLong(next.getKey()), (String) next.getValue()));

      ZendeskConfig.INSTANCE.setCustomFields(fields);
    }

    Activity activity = getCurrentActivity();

    if(activity != null){
        Intent callSupportIntent = new Intent(activity, ContactZendeskActivity.class);
        callSupportIntent.putExtra(ContactZendeskActivity.EXTRA_CONTACT_CONFIGURATION, configuration);
        activity.startActivityForResult(callSupportIntent, REQUEST_CODE, null);
    }
  }

  @ReactMethod
  public void supportHistory() {

    Activity activity = getCurrentActivity();

    if(activity != null){
        Intent supportHistoryIntent = new Intent(getReactApplicationContext(), RequestActivity.class);
        supportHistoryIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getReactApplicationContext().startActivity(supportHistoryIntent);
    }
  }

}
