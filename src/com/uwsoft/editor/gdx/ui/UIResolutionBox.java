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

package com.uwsoft.editor.gdx.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.util.dialog.DialogUtils;
import com.puremvc.patterns.proxy.Proxy;
import com.uwsoft.editor.mvc.proxy.TextureManager;
import com.uwsoft.editor.mvc.view.stage.UIStage;
import com.uwsoft.editor.mvc.Overlap2DFacade;
import com.uwsoft.editor.mvc.proxy.ProjectManager;
import com.uwsoft.editor.mvc.proxy.ResolutionManager;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.data.ResolutionEntryVO;

public class UIResolutionBox extends Group {

    private final String curResolution;
    private final Overlap2DFacade facade;
    private final ProjectManager projectManager;
    private final TextureManager textureManager;

    private UIStage stage;

    private SelectBox<String> dropdown;

    private ProjectInfoVO projectInfoVO;

    public UIResolutionBox(UIStage s, ProjectInfoVO prjVo, String curResolution) {
        facade = Overlap2DFacade.getInstance();
        textureManager = facade.retrieveProxy(TextureManager.NAME);
        this.stage = s;

        this.projectInfoVO = prjVo;

        this.curResolution = curResolution;
        projectManager = facade.retrieveProxy(ProjectManager.NAME);
        int padding = 5;

        String[] arr = new String[projectInfoVO.resolutions.size() + 1];

        arr[0] = projectInfoVO.originalResolution.toString();
        int selectedIndex = 0;
        for (int i = 0; i < projectInfoVO.resolutions.size(); i++) {
            ResolutionEntryVO resolution = projectInfoVO.resolutions.get(i);
            String resolutionString = projectInfoVO.resolutions.get(i).toString();
            arr[i + 1] = resolutionString;
            if (resolution.name.equals(curResolution)) {
                selectedIndex = i + 1;
            }
        }

        dropdown = new SelectBox(textureManager.editorSkin);
        dropdown.setItems(arr);
        dropdown.setSelectedIndex(selectedIndex);
        dropdown.setWidth(150);
        addActor(dropdown);

        dropdown.setX(0);
        dropdown.setY(6);

        dropdown.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                loadCurrentResolution();

            }
        });

        TextButton delBtn = new TextButton("Delete", textureManager.editorSkin);
        delBtn.setX(dropdown.getX() + dropdown.getWidth() + padding);
        delBtn.setY(8);
        addActor(delBtn);

        TextButton createBtn = new TextButton("Create New", textureManager.editorSkin);
        createBtn.setX(delBtn.getX() + delBtn.getWidth() + padding);
        createBtn.setY(8);
        addActor(createBtn);

        TextButton repackBtn = new TextButton("Repack", textureManager.editorSkin);
        repackBtn.setX(createBtn.getX() + createBtn.getWidth() + padding);
        repackBtn.setY(8);
        addActor(repackBtn);

//        openBtn.addListener(new ClickListener() {
//            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
//                super.touchUp(event, x, y, pointer, button);
//
//                String res = "orig";
//
//                final int index = dropdown.getSelectedIndex();
//                if (index > 0) {
//                    res = projectInfoVO.resolutions.get(index - 1).name;
//                }
//                String name = stage.sandboxStage.getCurrentSceneVO().sceneName;
//                DataManager.getInstance().openProjectAndLoadAllData(DataManager.getInstance().getCurrentProjectVO().projectName, res);
//                stage.sandboxStage.loadCurrentProject(name);
//                stage.loadCurrentProject();
//
//            }
//        });

        createBtn.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                stage.dialogs().showCreateNewResolutionDialog();

            }
        });

        delBtn.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                final int index = dropdown.getSelectedIndex();
                if (index == 0) {
                    return;
                }

                ResolutionEntryVO resEntry = projectInfoVO.resolutions.get(index - 1);

                DialogUtils.showConfirmDialog(stage,
                        "Delete Resolution",
                        "Are you sure you want to delete resolution: " + resEntry.toString() + " ?",
                        new String[]{"Delete", "Cancel"}, new Integer[]{0, 1},
                        result -> {
                            if (result == 0) {
                                ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
                                resolutionManager.deleteResolution(index - 1);
                                String name = stage.getSandbox().sceneControl.getCurrentSceneVO().sceneName;
                                stage.getSandbox().loadCurrentProject(name);
                                stage.getSandbox().loadCurrentProject();
                                stage.getCompositePanel().initResolutionBox();
                            }
                        });

            }
        });

        repackBtn.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ResolutionManager resolutionManager = facade.retrieveProxy(ResolutionManager.NAME);
                resolutionManager.rePackProjectImagesForAllResolutions();
                loadCurrentResolution();
            }
        });

        setWidth(340);
    }

    private void loadCurrentResolution() {
        String res = "orig";
        final int index = dropdown.getSelectedIndex();
        if (index > 0) {
            res = projectInfoVO.resolutions.get(index - 1).name;
        }
        String name = stage.getSandbox().sceneControl.getCurrentSceneVO().sceneName;
        projectManager.openProjectAndLoadAllData(projectManager.getCurrentProjectVO().projectName, res);
        stage.getSandbox().loadCurrentProject(name);
        stage.loadCurrentProject();
    }
}
