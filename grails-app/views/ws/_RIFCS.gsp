<?xml version="1.0" encoding="UTF-8"?>
<%@ page import="au.org.ala.soils2sat.UserProfile; au.org.ala.soils2sat.User; au.org.ala.soils2sat.DateUtils" %>
<registryObjects xmlns="http://ands.org.au/standards/rif-cs/registryObjects" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://ands.org.au/standards/rif-cs/registryObjects http://services.ands.org.au/documentation/rifcs/schema/registryObjects.xsd">
    <g:each in="${extracts}" var="extract">
        <registryObject group="Terrestrial Ecosystem Research Network">
            <key>${extract.doi}</key>
            <originatingSource type="">http://researchdata.ands.org.au/registry//orca/register_my_data</originatingSource>
            <collection type="dataset">
                <name type="primary">
                    <namePart>Ecological Data for TREND Site(s) ${locationMap[extract]?.join(", ")}</namePart>
                </name>
                <description type="brief">&lt;p&gt;S2S brings together data from a broad range of custodians. Open remote sensing products are available from the Bureau of Meteorology and TERN AusCover. These have been leveraged by CSIRO and the ALA, making available valuable spatial layers representing different types of climate and environmental data. These data can then be further overlaid with soil maps that are created by CSIRO and TERN’s Soils and Landscape Grid of Australia facility on demand by the user. Biodiversity data (initially plant species data only, to be extended to animal species in the future) is also integrated into this system and these data have been contributed to the ALA by its partners − Commonwealth and State Herbariums and Museums, government agencies and the public. The raw ecological plot data available in ÆKOS presently has databases and datasets contributed by government agencies and TERN’s Multi Scaled Plot Network (AusPlot Rangelands and the Australian Transect Network –TREND) and will also house a number of additional researcher datasets in the near future. The genetics data are contributed by the Barcode of Life, GenBank and BioPlatforms Australia to the ÆKOS.&lt;/p&gt;</description>
                <description type="note">&lt;p id="docs-internal-guid-7221c1e4-7995-9c58-c875-17fbd30ca370" dir="ltr"&gt;AusPlots Rangelands is a facility of the Terrestrial Ecosystems Research Network (TERN). The Australian Rangelands cover approximately 81% of the continent, including 52 bioregions represented by low rainfall and low nutrient soils. AusPlots is establishing a network of permanent monitoring plots throughout Australia’s Rangelands bioregions and is undertaking baseline surveys of vegetation and soils.&lt;/p&gt;
                &lt;p dir="ltr"&gt;TREND is a component of the Australian Transect Network, which is building a network of monitoring sites across key subcontinental-scale environmental gradients. TREND has been established on a temperature and rainfall gradient in the Flinders and Mt Lofty Ranges of South Australia to understand how these variables impact on native ecosystems and gain inferences as to how these systems will change with climate. &lt;/p&gt;
                &lt;p dir="ltr"&gt;The  Terrestrial Ecosystems Research Network, a collaboration of scientists and policy experts across Australia dedicating to exploring and understanding Australia’s ecosystems and landscapes.&lt;/p&gt;
                &lt;p&gt;All data has been collected using the AusPlots Rangelands Survey protocols, which are available at:&lt;a href="http://tern.org.au/AusPlots-Rangelands-Survey-Protocols-Manual-pg23944.html"&gt; http://tern.org.au/AusPlots-Rangelands-Survey-Protocols-Manual-pg23944.html&lt;/a&gt;&lt;/p&gt;</description>
                <rights>
                    <rightsStatement>Rights owned by The University of Adelaide. Rights licensed subject to TERN Attribution (TERN-BY) Data Licence v1.0.</rightsStatement>
                    <licence type="Unknown/Other" rightsUri="http://tern.org.au/rs/7/sites/998/user_uploads/File/Data%20Licensing%20Documents/TERN%20Attribution%20Data%20Licence%20v1_0.pdf">TERN Attribution (TERN-BY) Data Licence v1.0, TERN Attribution (TERN-BY) Data Licence v1.0. This licence lets the user distribute, remix, and build upon the data provided that they credit the original source and any other nominated parties. Attribution and citation is required as described at:</licence>
                    <accessRights type="" rightsUri="">This data is licensed under a TERN Attribution (TERN-BY) Data Licence v1.0, TERN Attribution (TERN-BY) Data Licence v1.0. We ask that you send us citations and copies of publications arising from work that use these data.</accessRights>
                </rights>
                <identifier type="local">${createLink(controller: 'extract', action:'landingPage',params:[packageName:extract.packageName], absolute: true)}</identifier>
                <dates type="dc.created">
                    <g:set var="now" value="${new Date()}" />
                    <date type="dateFrom" dateFormat="W3CDTF">${DateUtils.getW3CDTFDate(now)}</date>
                    <date type="dateTo" dateFormat="W3CDTF">${DateUtils.getW3CDTFDate(now)}</date>
                </dates>
                <location dateFrom="${DateUtils.getW3CDTFDate(now)}">
                    <address>
                        <electronic type="url">
                            <value>${createLink(uri: '/', absolute: true)}</value>
                        </electronic>
                    </address>
                </location>
                <coverage>
                    <temporal>
                        <date type="dateFrom" dateFormat="W3CDTF">${DateUtils.getW3CDTFDate(extract.firstVisitDate)}</date>
                        <date type="dateTo" dateFormat="W3CDTF">${DateUtils.getW3CDTFDate(extract.lastVisitDate)}</date>
                    </temporal>
                    <spatial type="kmlPolyCoords"></spatial>
                </coverage>
                <relatedObject>
                    <key>aekos.org.au/service-10</key>
                    <relation type="hasAssociationWith"/>
                </relatedObject>
                <relatedObject>
                    <key>aekos.org.au/service-1</key>
                    <relation type="hasAssociationWith"/>
                </relatedObject>
                <relatedObject>
                    <key>aekos.org.au/party-2</key>
                    <relation type="hasAssociationWith"/>
                </relatedObject>
                <relatedObject>
                    <key>Contributor:Atlas of Living Australia</key>
                    <relation type="hasAssociationWith"/>
                </relatedObject>
                <subject type="anzsrc-for">0502</subject>
                <subject type="anzsrc-for">0503</subject>
                <subject type="anzsrc-for">0604</subject>
                <citationInfo>
                    <g:set var="author" value="${User.findByUsername(extract.username)}" />
                    <g:set var="profile" value="${UserProfile.findByUser(author)}" />
                    <fullCitation>${profile?.fullName}. <g:formatDate date="${extract.date}" format="yyyy" /> Version ${extract.appVersion}. Soils2Satellite. ${extract.doi}. Obtained from ${createLink(uri:'/', absolute: true)}, made available by the Eco-Informatics Facility (http://www.tern.org.au/Eco-informatics-pg17733.html) of the Terrestrial Ecosystem Research Network (TERN, http://www.tern.org.au ). Accessed Date <g:formatDate date="${extract.date}" format="${DateUtils.S2S_DATE_TIME_FORMAT}" />.</fullCitation>
                </citationInfo>
            </collection>
        </registryObject>
    </g:each>
</registryObjects>



