/*
 * Copyright 2014 Jakub Spatny
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kubaspatny.simplecounter;

public class WidgetUtils {
	
	public final static String WIDGET_UPDATE_ACTION = "com.kubaspatny.simplecounter.widget.ACTION_UPDATE";
	public final static String WIDGET_INCREMENT_ACTION = "com.kubaspatny.simplecounter.widget.ACTION_INCREMENT";
	public final static String WIDGET_DECREMENT_ACTION = "com.kubaspatny.simplecounter.widget.ACTION_DECREMENT";
	public final static String WIDGET_OPENCOUNTER_ACTION = "com.kubaspatny.simplecounter.widget.ACTION_OPENCOUNTER";
	public final static String SHARED_PREFERENCES_NAME = "com.kubaspatny.simplecounter.widgetID";
	
	// _ID from DB
	public final static String SHARED_PREFERENCES_ROW = "rowID";
	
	// 1 - light
	// 2 - dark
	// 3 - CARD_COLOR
	public final static String SHARED_PREFERENCES_COLORFUL = "colorful";
	
	
	// using as SHARED_PREFERENCES_NAME + id + SHARED_PREFERENCES_ROW
	// using as SHARED_PREFERENCES_NAME + id + SHARED_PREFERENCES_COLORFUL
	
	public final static int WIDGETS_COUNTER_DELETED = -2;

}
 