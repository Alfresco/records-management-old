/*
 * Copyright (C) 2005-2015 Alfresco Software Limited.
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

package org.alfresco.test.integration.classify;

import static org.junit.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

import org.alfresco.po.rm.details.record.ClassifiedPropertiesPanel;
import org.alfresco.po.share.browse.documentlibrary.DocumentIndicators;
import org.alfresco.po.share.browse.documentlibrary.DocumentLibrary;
import org.alfresco.po.share.details.document.ClassifiedDocumentDetails;
import org.alfresco.test.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Classify document integration test
 *
 * @author David Webster
 * @author tpage
 * @since 3.0
 */
public class BrowseClassifiedDocuments extends BaseTest
{
    @Autowired
    private DocumentLibrary documentLibrary;
    @Autowired
    private ClassifiedDocumentDetails classifiedDocumentDetails;
    @Autowired
    private ClassifiedPropertiesPanel classifiedPropertiesPanel;

    /** Check the classified document indicator. */
    @Test
    (
        groups = { "integration" },
        description = "Verify Classify Document behaviour",
        dependsOnGroups = { GROUP_SECRET_DOCUMENT_EXISTS }
    )
    public void classifyDocumentIndicator()
    {
        // Open Collab site DocumentLibrary.
        openPage(documentLibrary, COLLAB_SITE_ID);

        // verify document actions
        assertTrue(documentLibrary.getDocument(SECRET_DOCUMENT)
            .hasIndicator(DocumentIndicators.CLASSIFIED));
    }

    /** Verify document details page displays classification properties. */
    @Test
    (
        groups = { "integration" },
        description = "Verify document details page displays classification properties",
        dependsOnGroups = { GROUP_SECRET_DOCUMENT_EXISTS }
    )
    public void classifyDocumentProperties()
    {
        // Open Collab site DocumentLibrary.
        openPage(documentLibrary, COLLAB_SITE_ID);

        // navigate to the document details page
        documentLibrary.getDocument(SECRET_DOCUMENT)
            .clickOnLink(classifiedDocumentDetails);

        // verify that classification is as expected.
        assertEquals(SECRET_CLASSIFICATION_LEVEL_TEXT,
                    classifiedPropertiesPanel.getClassifiedProperty(ClassifiedPropertiesPanel.CURRENT_CLASSIFICATION));

        assertEquals(CLASSIFICATION_AUTHORITY,
                    classifiedPropertiesPanel.getClassifiedProperty(ClassifiedPropertiesPanel.CLASSIFICATION_AUTHORITY));

        assertEquals(CLASSIFICATION_REASON,
                    classifiedPropertiesPanel.getClassifiedProperty(ClassifiedPropertiesPanel.CLASSIFICATION_REASON));
    }

    /**
     * User with highest clearance can view all documents.
     * <p>
     * <a href="https://issues.alfresco.com/jira/browse/RM-2286">RM-2286</a><pre>
     * Given that I am a user with the highest possible security clearance
     * When I browse the document library
     * Then I can see all unclassified documents
     * And I can see all classified documents
     * </pre>
     */
    @Test
    (
        groups = { "integration" },
        description = "User with highest clearance can view all documents.",
        dependsOnGroups = { GROUP_TOP_SECRET_DOCUMENT_EXISTS, GROUP_SECRET_DOCUMENT_EXISTS, GROUP_UNCLASSIFIED_DOCUMENT_EXISTS }
    )
    public void highestClearanceUserCanViewEverything()
    {
        openPage(documentLibrary, COLLAB_SITE_ID);
        // Check that we can access the documents by viewing their names.
        assertTrue(documentLibrary.getDocument(TOP_SECRET_DOCUMENT).getName().startsWith(TOP_SECRET_DOCUMENT));
        assertTrue(documentLibrary.getDocument(SECRET_DOCUMENT).getName().startsWith(SECRET_DOCUMENT));
        assertTrue(documentLibrary.getDocument(UNCLASSIFIED_DOCUMENT).getName().startsWith(UNCLASSIFIED_DOCUMENT));
    }
}