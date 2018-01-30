/**
 * Created by Patrick O'Connor on 8/30/17.
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

public class FeedbackConfig extends BaseZendeskFeedbackConfiguration {

    private ReadableMap options;

    public FeedbackConfig(ReadableMap options){
      super();
      this.options = options;
    }

    @Override
    public String getRequestSubject() {
      if(!this.options.hasKey("subject"))
        return null;
      return this.options.getString("subject");
    }

    @Override
    public List<String> getTags() {
      if(!this.options.hasKey("tags"))
        return null;
      ReadableArray tagsArray = this.options.getArray("tags");
      List<String> tags = new ArrayList(tagsArray.size());
      for(int i = 0; i < tagsArray.size(); i++){
        tags.add(tagsArray.getString(i));
      }
      return tags;
    }
  }

public class RNZenDeskSupportModule extends ReactContextBaseJavaModule {
  public RNZenDeskSupportModule(ReactApplicationContext reactContext) {
    super(reactContext);
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
    Identity zdIdentity = new AnonymousIdentity.Builder()
      .withEmailIdentifier(identity.getString("customerEmail"))
      .withNameIdentifier(identity.getString("customerName"))
      .build();

    ZendeskConfig.INSTANCE.setIdentity(zdIdentity);
  }

  @ReactMethod
  public void showHelpCenterWithOptions(ReadableMap options) {
    Boolean showConversationsMenuButton = true;
    Boolean articleVotingEnabled = true;
    ContactUsButtonVisibility withContactUsButtonVisibility = ContactUsButtonVisibility.ARTICLE_LIST_AND_ARTICLE;
    if (!(options == null || options.toHashMap().isEmpty())) {
      if (options.hasKey("showConversationsMenuButton")) {
        showConversationsMenuButton = options.getBoolean("showConversationsMenuButton");
      }
      if (options.hasKey("articleVotingEnabled")) {
        articleVotingEnabled = options.getBoolean("articleVotingEnabled");
      }
      if (options.hasKey("withContactUsButtonVisibility")) {
        switch(options.getString("withContactUsButtonVisibility")) {
          case "OFF":
            withContactUsButtonVisibility = ContactUsButtonVisibility.OFF;
            break;
          case "ARTICLE_LIST_ONLY":
            withContactUsButtonVisibility = ContactUsButtonVisibility.ARTICLE_LIST_ONLY;
            break;
          case "ARTICLE_LIST_AND_ARTICLE":
          default:
            withContactUsButtonVisibility = ContactUsButtonVisibility.ARTICLE_LIST_AND_ARTICLE;
        }
      }
    }
    new SupportActivity
      .Builder()
      .withArticleVoting(articleVotingEnabled)
      .withContactUsButtonVisibility(withContactUsButtonVisibility)
      .showConversationsMenuButton(showConversationsMenuButton)
      .show(getReactApplicationContext());
  }

  @ReactMethod
  public void showCategoriesWithOptions(ReadableArray categoryIds, ReadableMap options) {
    Boolean showConversationsMenuButton = true;
    Boolean articleVotingEnabled = true;
    ContactUsButtonVisibility withContactUsButtonVisibility = ContactUsButtonVisibility.ARTICLE_LIST_AND_ARTICLE;
    if (!(options == null || options.toHashMap().isEmpty())) {
      if (options.hasKey("showConversationsMenuButton")) {
        showConversationsMenuButton = options.getBoolean("showConversationsMenuButton");
      }
      if (options.hasKey("articleVotingEnabled")) {
        articleVotingEnabled = options.getBoolean("articleVotingEnabled");
      }
      if (options.hasKey("withContactUsButtonVisibility")) {
        switch(options.getString("withContactUsButtonVisibility")) {
          case "OFF":
            withContactUsButtonVisibility = ContactUsButtonVisibility.OFF;
            break;
          case "ARTICLE_LIST_ONLY":
            withContactUsButtonVisibility = ContactUsButtonVisibility.ARTICLE_LIST_ONLY;
            break;
          case "ARTICLE_LIST_AND_ARTICLE":
          default:
            withContactUsButtonVisibility = ContactUsButtonVisibility.ARTICLE_LIST_AND_ARTICLE;
        }
      }
    }
    new SupportActivity
      .Builder()
      .withArticleVoting(articleVotingEnabled)
      .withContactUsButtonVisibility(withContactUsButtonVisibility)
      .showConversationsMenuButton(showConversationsMenuButton)
      .withArticlesForCategoryIds(toLongArray(categoryIds.toArrayList()))
      .show(getReactApplicationContext());
  }

  @ReactMethod
  public void showSectionsWithOptions(ReadableArray sectionIds, ReadableMap options) {
    Boolean showConversationsMenuButton = true;
    Boolean articleVotingEnabled = true;
    ContactUsButtonVisibility withContactUsButtonVisibility = ContactUsButtonVisibility.ARTICLE_LIST_AND_ARTICLE;
    if (!(options == null || options.toHashMap().isEmpty())) {
      if (options.hasKey("showConversationsMenuButton")) {
        showConversationsMenuButton = options.getBoolean("showConversationsMenuButton");
      }
      if (options.hasKey("articleVotingEnabled")) {
        articleVotingEnabled = options.getBoolean("articleVotingEnabled");
      }
      if (options.hasKey("withContactUsButtonVisibility")) {
        switch(options.getString("withContactUsButtonVisibility")) {
          case "OFF":
            withContactUsButtonVisibility = ContactUsButtonVisibility.OFF;
            break;
          case "ARTICLE_LIST_ONLY":
            withContactUsButtonVisibility = ContactUsButtonVisibility.ARTICLE_LIST_ONLY;
            break;
          case "ARTICLE_LIST_AND_ARTICLE":
          default:
            withContactUsButtonVisibility = ContactUsButtonVisibility.ARTICLE_LIST_AND_ARTICLE;
        }
      }
    }
    new SupportActivity
      .Builder()
      .withArticleVoting(articleVotingEnabled)
      .withContactUsButtonVisibility(withContactUsButtonVisibility)
      .showConversationsMenuButton(showConversationsMenuButton)
      .show(getReactApplicationContext());
  }

  @ReactMethod
  public void showLabelsWithOptions(ReadableArray labels, ReadableMap options) {
    Boolean showConversationsMenuButton = true;
    Boolean articleVotingEnabled = true;
    ContactUsButtonVisibility withContactUsButtonVisibility = ContactUsButtonVisibility.ARTICLE_LIST_AND_ARTICLE;
    if (!(options == null || options.toHashMap().isEmpty())) {
      if (options.hasKey("showConversationsMenuButton")) {
        showConversationsMenuButton = options.getBoolean("showConversationsMenuButton");
      }
      if (options.hasKey("articleVotingEnabled")) {
        articleVotingEnabled = options.getBoolean("articleVotingEnabled");
      }
      if (options.hasKey("withContactUsButtonVisibility")) {
        switch(options.getString("withContactUsButtonVisibility")) {
          case "OFF":
            withContactUsButtonVisibility = ContactUsButtonVisibility.OFF;
            break;
          case "ARTICLE_LIST_ONLY":
            withContactUsButtonVisibility = ContactUsButtonVisibility.ARTICLE_LIST_ONLY;
            break;
          case "ARTICLE_LIST_AND_ARTICLE":
          default:
            withContactUsButtonVisibility = ContactUsButtonVisibility.ARTICLE_LIST_AND_ARTICLE;
        }
      }
    }
    //noinspection SuspiciousToArrayCall
    new SupportActivity
      .Builder()
      .withArticleVoting(articleVotingEnabled)
      .withContactUsButtonVisibility(withContactUsButtonVisibility)
      .showConversationsMenuButton(showConversationsMenuButton)
      .withLabelNames(labels.toArrayList().toArray(new String[]{}))
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
  public void callSupport(ReadableMap options) {

    ZendeskFeedbackConfiguration configuration = new FeedbackConfig(options);

    if(options.hasKey("customFields")){
      List<CustomField> fields = new ArrayList<>();

      for (Map.Entry<String, Object> next : options.getMap("customFields").toHashMap().entrySet())
        fields.add(new CustomField(Long.parseLong(next.getKey()), (String) next.getValue()));

      ZendeskConfig.INSTANCE.setCustomFields(fields);
    }

    Activity activity = getCurrentActivity();

    if(activity != null){
        Intent callSupportIntent = new Intent(getReactApplicationContext(), ContactZendeskActivity.class);
        callSupportIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callSupportIntent.putExtra(ContactZendeskActivity.EXTRA_CONTACT_CONFIGURATION, configuration);
        getReactApplicationContext().startActivity(callSupportIntent);
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
