<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
    <xsl:template match="page">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="simpleA4" page-height="20cm" page-width="8cm" margin="5mm">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="simpleA4">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block font-size="8pt">
                        <xsl:apply-templates select="header"/>
                    </fo:block>
                    <fo:block><xsl:text>&#160;</xsl:text></fo:block>
                    <fo:block font-size="8pt">
                        <xsl:apply-templates select="body"/>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    <xsl:template match="header">
        <fo:table table-layout="fixed" width="100%">
            <fo:table-column column-width="100%"/>
            <fo:table-body text-align="center">
                <fo:table-row>
                    <fo:table-cell border-width="1px" border-style="solid" display-align="center" text-align="center" padding="2mm">
                        <fo:block font-size="12pt" font-style="italic">
                            <xsl:value-of select="company/name"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row>
                    <fo:table-cell padding-top="1mm" padding-bottom="1mm">
                        <fo:block>
                            <fo:inline font-weight="bold">Address:</fo:inline>
                            <fo:block/>
                            <xsl:value-of select="company/address"/>
                        </fo:block>
                        <fo:block>
                            <fo:inline font-weight="bold">Phone:</fo:inline>
                            <xsl:value-of select="company/phone"/>
                        </fo:block>
                        <fo:block>
                            <fo:inline font-weight="bold">Email:</fo:inline>
                            <xsl:value-of select="company/email"/>
                        </fo:block>
                        <fo:block>
                            <fo:inline font-weight="bold">Website:</fo:inline>
                            <xsl:value-of select="company/website"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
                <fo:table-row border-top-width="1px" border-top-style="solid">
                    <fo:table-cell padding-top="1mm">
                        <fo:block>
                            <fo:inline font-weight="bold">InvoiceID:</fo:inline>
                            <fo:block/>
                            <xsl:value-of select="invoice/id"/>
                        </fo:block>
                        <fo:block>
                            <fo:inline font-weight="bold">Date:</fo:inline> <xsl:value-of select="invoice/date"/>
                        </fo:block>
                        <fo:block linefeed-treatment="preserve">
                            <fo:inline font-weight="bold">Bill to:</fo:inline>
                            <fo:block/>
                            <xsl:value-of select="invoice/billTo"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </xsl:template>
    <xsl:template match="body">
        <fo:table table-layout="fixed" width="100%">
            <fo:table-column column-width="40%"/>
            <fo:table-column column-width="20%"/>
            <fo:table-column column-width="20%"/>
            <fo:table-column column-width="20%"/>
            <fo:table-header  margin-left="1mm">
                <fo:table-row>
                    <fo:table-cell border-width="1px" border-style="solid" padding="1mm">
                        <fo:block font-weight="bold">Product</fo:block>
                    </fo:table-cell>
                    <fo:table-cell border-width="1px" border-style="solid" padding="1mm">
                        <fo:block font-weight="bold">Qty.</fo:block>
                    </fo:table-cell>
                    <fo:table-cell border-width="1px" border-style="solid" padding="1mm">
                        <fo:block font-weight="bold">Price</fo:block>
                    </fo:table-cell>
                    <fo:table-cell border-width="1px" border-style="solid" padding="1mm">
                        <fo:block font-weight="bold">Total</fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-header>
            <fo:table-body>
                <xsl:for-each select="items/item">
                    <fo:table-row display-align="center">
                        <fo:table-cell border-width="1px" border-style="solid" padding="1mm">
                            <fo:block>
                                <xsl:value-of select="productName"/>
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell border-width="1px" border-style="solid" padding="1mm">
                            <fo:block>
                                <xsl:value-of select="quantity"/>
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell border-width="1px" border-style="solid" padding="1mm">
                            <fo:block>
                                <xsl:value-of select="price"/>
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell border-width="1px" border-style="solid" padding="1mm">
                            <fo:block>
                                <xsl:value-of select="total"/>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </xsl:for-each>
                <xsl:for-each select="sums/sum">
                    <fo:table-row>
                        <fo:table-cell border-width="1px"><fo:block></fo:block></fo:table-cell>
                        <fo:table-cell border-width="1px" border-style="solid" background-color="grey" padding="1mm" number-columns-spanned="2" text-align="right">
                            <fo:block font-weight="bold">
                                <xsl:value-of select="key"/>
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell border-width="1px" border-style="solid" padding="1mm">
                            <fo:block>
                                <xsl:value-of select="value"/>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </xsl:for-each>
            </fo:table-body>
        </fo:table>
        <fo:block><xsl:text>&#160;</xsl:text></fo:block>
        <fo:table table-layout="fixed" width="100%">
            <fo:table-column column-width="100%"/>
            <fo:table-body margin-left="1mm">
                <xsl:for-each select="infos/info">
                    <fo:table-row>
                        <fo:table-cell background-color="grey" font-weight="bold" display-align="center" text-align="center" padding="1mm">
                            <fo:block>
                                Important notice
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                    <fo:table-row>
                        <fo:table-cell>
                            <fo:block>
                                <xsl:value-of select="text"/>
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </xsl:for-each>
            </fo:table-body>
        </fo:table>
    </xsl:template>
</xsl:stylesheet>