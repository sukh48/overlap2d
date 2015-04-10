/*
 * ******************************************************************************
 *  * Copyright 2015 See AUTHORS file.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package com.uwsoft.editor.mvc.view.ui.box;

import com.puremvc.patterns.mediator.SimpleMediator;

/**
 * Created by sargis on 4/10/15.
 */
public class UIItemsTreeBoxMediator extends SimpleMediator<UIItemsTreeBox> {
    private static final String TAG = UIItemsTreeBoxMediator.class.getCanonicalName();
    public static final String NAME = TAG;

    public UIItemsTreeBoxMediator() {
        super(NAME, new UIItemsTreeBox());
    }
}
