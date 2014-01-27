/*
 * Copyright (C) 2005-2014 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.po.rm;

import org.alfresco.po.RMFactoryPage;
import org.alfresco.po.share.SharePage;
import org.alfresco.po.share.site.UploadFilePage;
import org.alfresco.webdrone.HtmlElement;
import org.alfresco.webdrone.HtmlPage;
import org.alfresco.webdrone.RenderTime;
import org.alfresco.webdrone.WebDrone;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

/**
 * This class extends the {@link UploadFilePage} in order to make filing records possible.
 * This is a temporary solution. After moving RM related classes to its own module this class won't be used anymore.
 *
 * @author Tuna Aksoy
 * @since 2.2
 */
public class RMUploadFilePage extends UploadFilePage
{
    private Log logger = LogFactory.getLog(this.getClass());

    public RMUploadFilePage(WebDrone drone)
    {
        super(drone);
    }

    @SuppressWarnings("unchecked")
    @Override
    public RMUploadFilePage render(RenderTime timer)
    {
        basicRender(timer);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public RMUploadFilePage render()
    {
        return render(new RenderTime(maxPageLoadingTime));
    }

    @SuppressWarnings("unchecked")
    @Override
    public RMUploadFilePage render(final long time)
    {
        return render(new RenderTime(time));
    }

    /**
     * Action that selects the submit upload button.
     * @return boolean true if submitted.
     */
    private void submitUpload()
    {
        By selector;
        if(alfrescoVersion.isCloud())
        {
            selector = By.id("template_x002e_dnd-upload_x002e_documentlibrary_x0023_default-cancelOk-button-button");
        }
        else
        {
            selector = By.cssSelector("button[id*='html-upload']");
        }
        try
        {
            HtmlElement okButton = new HtmlElement(drone.find(selector), drone);
            String ready = okButton.click();
            if(logger.isTraceEnabled())
            {
                logger.trace(String.format("operation completed in: %s",ready));
            }
            while(true)
            {
                try
                {
                    //Verify button has been actioned
                    if(!drone.find(selector).isDisplayed())
                    {
                        break;
                    }
                }
                catch (NoSuchElementException e)
                {
                    break;
                }
            }

        }
        //Check result has been updated
        catch (TimeoutException te){}
    }

    /**
     * Uploads a file by entering the file location into the input field and
     * submitting the form.
     *
     * @param filePath String file location to upload
     * @return {@link SharePage} DocumentLibrary or a RepositoryPage response
     */
    public HtmlPage uploadFile(final String filePath)
    {
        WebElement input;
        if(alfrescoVersion.isFileUploadHtml5())
        {
            input = drone.find(By.cssSelector("input.dnd-file-selection-button"));
            input.sendKeys(filePath);
        }
        else
        {
            input = drone.find(By.cssSelector("input[id$='default-filedata-file']"));
            input.sendKeys(filePath);
            submitUpload();
        }

        if (logger.isTraceEnabled())
        {
            logger.trace("Upload button has been actioned");
        }

        FilePlanPage filePlanPage = RMFactoryPage.getPage(getCurrentUrl(), drone).render();

        // FIXME: This is a workaround. The render method must be changed for {@link FilePlanPage}
        drone.waitFor(3000);

        if (filePlanPage.hasFiles() == false)
        {
            throw new RuntimeException("A file should have been uploaded!");
        }
        return filePlanPage;
    }

    private String getCurrentUrl()
    {
        String currentUrl = drone.getCurrentUrl();
        if (StringUtils.contains(currentUrl, "#"))
        {
            currentUrl = drone.getCurrentUrl().split("#")[0];
        }
        return currentUrl;
    }
}