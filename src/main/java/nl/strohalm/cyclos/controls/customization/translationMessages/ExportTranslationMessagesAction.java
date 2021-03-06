/*
 This file is part of Cyclos.

 Cyclos is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 Cyclos is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Cyclos; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 */
package nl.strohalm.cyclos.controls.customization.translationMessages;

import java.util.Properties;

import javax.servlet.http.HttpServletResponse;

import nl.strohalm.cyclos.annotations.Inject;
import nl.strohalm.cyclos.controls.ActionContext;
import nl.strohalm.cyclos.controls.BaseAction;
import nl.strohalm.cyclos.controls.BaseAjaxAction.ContentType;
import nl.strohalm.cyclos.entities.settings.LocalSettings;
import nl.strohalm.cyclos.services.customization.TranslationMessageService;
import nl.strohalm.cyclos.utils.ResponseHelper;
import nl.strohalm.cyclos.utils.SettingsHelper;

import org.apache.struts.action.ActionForward;

/**
 * Action used to export messages into a properties file
 * @author luis
 */
public class ExportTranslationMessagesAction extends BaseAction {

    private TranslationMessageService translationMessageService;

    @Inject
    public void setTranslationMessageService(final TranslationMessageService translationMessageService) {
        this.translationMessageService = translationMessageService;
    }

    @Override
    protected ActionForward executeAction(final ActionContext context) throws Exception {

        final LocalSettings localSettings = SettingsHelper.getLocalSettings(getServlet().getServletContext());
        final String language = localSettings.getLanguage().getValue();
        final HttpServletResponse response = context.getResponse();

        // Prepare the response
        response.setContentType(ContentType.TEXT.getContentType());
        ResponseHelper.setDownload(response, "ApplicationResources_" + language + ".properties");

        // Write the properties file to the output stream
        final Properties properties = translationMessageService.exportAsProperties();
        final String comments = String.format("%s (%s)", localSettings.getApplicationName(), language);
        properties.store(response.getOutputStream(), comments);

        // The response is complete
        return null;
    }

}
