<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format" exclude-result-prefixes="fo">
    <xsl:template match="page">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="simpleA4" page-height="29.7cm" page-width="21cm" margin-top="2cm" margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="simpleA4">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block>
                        <xsl:apply-templates select="header"/>
                    </fo:block>
                    <fo:block><xsl:text>&#160;</xsl:text></fo:block>
                    <fo:block font-size="10pt">
                        <xsl:apply-templates select="body"/>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    <xsl:template match="header">
        <fo:table table-layout="fixed" width="100%">
            <fo:table-column column-width="50%"/>
            <fo:table-column column-width="50%"/>
            <fo:table-body font-size="10pt">
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block font-size="16pt" font-style="italic">
                            <xsl:value-of select="company/name"/>
                        </fo:block>
                        <fo:block><xsl:text>&#160;</xsl:text></fo:block>
                        <fo:block>
                            Address: <xsl:value-of select="company/address"/>
                        </fo:block>
                        <fo:block>
                            Phone: <xsl:value-of select="company/phone"/>
                        </fo:block>
                        <fo:block>
                            Email: <xsl:value-of select="company/email"/>
                        </fo:block>
                        <fo:block>
                            Website: <xsl:value-of select="company/website"/>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell margin-left="2cm">
                        <fo:block font-size="16pt">
                            <xsl:text>&#160;</xsl:text>
                        </fo:block>
                        <fo:block><xsl:text>&#160;</xsl:text></fo:block>
                        <fo:block>
                            InvoiceID: <xsl:value-of select="invoice/id"/>
                        </fo:block>
                        <fo:block>
                            Date: <xsl:value-of select="invoice/date"/>
                        </fo:block>
                        <fo:block>
                            Bill to: <xsl:value-of select="invoice/billTo"/>
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
                        <fo:block font-weight="bold">Quantity</fo:block>
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
                        <fo:table-cell border-width="1px"><fo:block></fo:block></fo:table-cell>
                        <fo:table-cell border-width="1px" border-style="solid" background-color="grey" padding="1mm">
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
            <fo:table-column column-width="20%"/>
            <fo:table-column column-width="80%"/>
            <fo:table-body margin-left="1mm">
                <xsl:for-each select="infos/info">
                    <fo:table-row>
                        <fo:table-cell background-color="grey" font-weight="bold" display-align="center" text-align="center">
                            <fo:block>
                                Important notice:
                            </fo:block>
                        </fo:table-cell>
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