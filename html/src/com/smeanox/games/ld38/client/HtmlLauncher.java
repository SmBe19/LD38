package com.smeanox.games.ld38.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.LD38;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(Consts.DESIGN_WIDTH, Consts.DESIGN_HEIGHT);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new LD38();
        }
}