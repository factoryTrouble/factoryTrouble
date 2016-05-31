/*
FactoryTrouble
Copyright (C) 2016  Projectmembers of FacoriyTrouble

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 */
package de.uni_bremen.factroytrouble.spring;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class InitSpring {

    /**
     * Logger der Klasse InitSpring.
     */
    public static final Logger LOGGER = Logger.getLogger(InitSpring.class);

    /**
     * Initialisiert den Spring Kontext für die Applikation und gibt diesen zurück.
     *
     * @return Spring Kontext
     */
    public static ApplicationContext init() {
        LOGGER.info("Init Spring Context");
        ApplicationContext springContext = new AnnotationConfigApplicationContext(SpringConfig.class);
        SpringConfigHolder.getInstance().setContext(springContext);
        springContext.getBean(PostConstructTaskScheduler.class).execute();
        return springContext;
    }

}
