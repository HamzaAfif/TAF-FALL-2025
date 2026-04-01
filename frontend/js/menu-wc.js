'use strict';

customElements.define('compodoc-menu', class extends HTMLElement {
    constructor() {
        super();
        this.isNormalMode = this.getAttribute('mode') === 'normal';
    }

    connectedCallback() {
        this.render(this.isNormalMode);
    }

    render(isNormalMode) {
        let tp = lithtml.html(`
        <nav>
            <ul class="list">
                <li class="title">
                    <a href="index.html" data-type="index-link">TAF UI</a>
                </li>

                <li class="divider"></li>
                ${ isNormalMode ? `<div id="book-search-input" role="search"><input type="text" placeholder="Type to search"></div>` : '' }
                <li class="chapter">
                    <a data-type="chapter-link" href="index.html"><span class="icon ion-ios-home"></span>Getting started</a>
                    <ul class="links">
                                <li class="link">
                                    <a href="overview.html" data-type="chapter-link">
                                        <span class="icon ion-ios-keypad"></span>Overview
                                    </a>
                                </li>

                            <li class="link">
                                <a href="index.html" data-type="chapter-link">
                                    <span class="icon ion-ios-paper"></span>
                                        README
                                </a>
                            </li>
                                <li class="link">
                                    <a href="dependencies.html" data-type="chapter-link">
                                        <span class="icon ion-ios-list"></span>Dependencies
                                    </a>
                                </li>
                                <li class="link">
                                    <a href="properties.html" data-type="chapter-link">
                                        <span class="icon ion-ios-apps"></span>Properties
                                    </a>
                                </li>

                    </ul>
                </li>
                    <li class="chapter modules">
                        <a data-type="chapter-link" href="modules.html">
                            <div class="menu-toggler linked" data-bs-toggle="collapse" ${ isNormalMode ?
                                'data-bs-target="#modules-links"' : 'data-bs-target="#xs-modules-links"' }>
                                <span class="icon ion-ios-archive"></span>
                                <span class="link-name">Modules</span>
                                <span class="icon ion-ios-arrow-down"></span>
                            </div>
                        </a>
                        <ul class="links collapse " ${ isNormalMode ? 'id="modules-links"' : 'id="xs-modules-links"' }>
                            <li class="link">
                                <a href="modules/AppModule.html" data-type="entity-link" >AppModule</a>
                                    <li class="chapter inner">
                                        <div class="simple menu-toggler" data-bs-toggle="collapse" ${ isNormalMode ?
                                            'data-bs-target="#components-links-module-AppModule-b2cd52d3a1f9d2c5130b0e5d8da88ebbac39f9a9673041170ffa3dd63c32627d70a707f16d7c5e62615491062cecb9bfeeb6c4354630bea2504ce0b2e912f57d"' : 'data-bs-target="#xs-components-links-module-AppModule-b2cd52d3a1f9d2c5130b0e5d8da88ebbac39f9a9673041170ffa3dd63c32627d70a707f16d7c5e62615491062cecb9bfeeb6c4354630bea2504ce0b2e912f57d"' }>
                                            <span class="icon ion-md-cog"></span>
                                            <span>Components</span>
                                            <span class="icon ion-ios-arrow-down"></span>
                                        </div>
                                        <ul class="links collapse" ${ isNormalMode ? 'id="components-links-module-AppModule-b2cd52d3a1f9d2c5130b0e5d8da88ebbac39f9a9673041170ffa3dd63c32627d70a707f16d7c5e62615491062cecb9bfeeb6c4354630bea2504ce0b2e912f57d"' :
                                            'id="xs-components-links-module-AppModule-b2cd52d3a1f9d2c5130b0e5d8da88ebbac39f9a9673041170ffa3dd63c32627d70a707f16d7c5e62615491062cecb9bfeeb6c4354630bea2504ce0b2e912f57d"' }>
                                            <li class="link">
                                                <a href="components/AddTestDialogComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AddTestDialogComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/AppComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >AppComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/BoardAdminComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >BoardAdminComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/BoardUserComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >BoardUserComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/BusySpinnerComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >BusySpinnerComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/DeleteTestDialogComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >DeleteTestDialogComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/GatlingApiComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >GatlingApiComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/HomeComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >HomeComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/JmeterApiComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >JmeterApiComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/LoginComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >LoginComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/PerformanceTestApiComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >PerformanceTestApiComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ProfileComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >ProfileComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/ProjectComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >ProjectComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/RegisterComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >RegisterComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/TestApiComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >TestApiComponent</a>
                                            </li>
                                            <li class="link">
                                                <a href="components/TestSeleniumComponent.html" data-type="entity-link" data-context="sub-entity" data-context-id="modules" >TestSeleniumComponent</a>
                                            </li>
                                        </ul>
                                    </li>
                            </li>
                            <li class="link">
                                <a href="modules/AppRoutingModule.html" data-type="entity-link" >AppRoutingModule</a>
                            </li>
                            <li class="link">
                                <a href="modules/PerformanceTestApiModule.html" data-type="entity-link" >PerformanceTestApiModule</a>
                            </li>
                </ul>
                </li>
                    <li class="chapter">
                        <div class="simple menu-toggler" data-bs-toggle="collapse" ${ isNormalMode ? 'data-bs-target="#classes-links"' :
                            'data-bs-target="#xs-classes-links"' }>
                            <span class="icon ion-ios-paper"></span>
                            <span>Classes</span>
                            <span class="icon ion-ios-arrow-down"></span>
                        </div>
                        <ul class="links collapse " ${ isNormalMode ? 'id="classes-links"' : 'id="xs-classes-links"' }>
                            <li class="link">
                                <a href="classes/GatlingRequest.html" data-type="entity-link" >GatlingRequest</a>
                            </li>
                            <li class="link">
                                <a href="classes/JMeterFTPRequest.html" data-type="entity-link" >JMeterFTPRequest</a>
                            </li>
                            <li class="link">
                                <a href="classes/JMeterHttpRequest.html" data-type="entity-link" >JMeterHttpRequest</a>
                            </li>
                            <li class="link">
                                <a href="classes/ResponseTimePerPercentile.html" data-type="entity-link" >ResponseTimePerPercentile</a>
                            </li>
                            <li class="link">
                                <a href="classes/testModel.html" data-type="entity-link" >testModel</a>
                            </li>
                        </ul>
                    </li>
                        <li class="chapter">
                            <div class="simple menu-toggler" data-bs-toggle="collapse" ${ isNormalMode ? 'data-bs-target="#injectables-links"' :
                                'data-bs-target="#xs-injectables-links"' }>
                                <span class="icon ion-md-arrow-round-down"></span>
                                <span>Injectables</span>
                                <span class="icon ion-ios-arrow-down"></span>
                            </div>
                            <ul class="links collapse " ${ isNormalMode ? 'id="injectables-links"' : 'id="xs-injectables-links"' }>
                                <li class="link">
                                    <a href="injectables/AuthService.html" data-type="entity-link" >AuthService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/PerformanceTestApiService.html" data-type="entity-link" >PerformanceTestApiService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/TestApiService.html" data-type="entity-link" >TestApiService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/TokenStorageService.html" data-type="entity-link" >TokenStorageService</a>
                                </li>
                                <li class="link">
                                    <a href="injectables/UserService.html" data-type="entity-link" >UserService</a>
                                </li>
                            </ul>
                        </li>
                    <li class="chapter">
                        <div class="simple menu-toggler" data-bs-toggle="collapse" ${ isNormalMode ? 'data-bs-target="#interceptors-links"' :
                            'data-bs-target="#xs-interceptors-links"' }>
                            <span class="icon ion-ios-swap"></span>
                            <span>Interceptors</span>
                            <span class="icon ion-ios-arrow-down"></span>
                        </div>
                        <ul class="links collapse " ${ isNormalMode ? 'id="interceptors-links"' : 'id="xs-interceptors-links"' }>
                            <li class="link">
                                <a href="interceptors/AuthInterceptor.html" data-type="entity-link" >AuthInterceptor</a>
                            </li>
                        </ul>
                    </li>
                    <li class="chapter">
                        <div class="simple menu-toggler" data-bs-toggle="collapse" ${ isNormalMode ? 'data-bs-target="#interfaces-links"' :
                            'data-bs-target="#xs-interfaces-links"' }>
                            <span class="icon ion-md-information-circle-outline"></span>
                            <span>Interfaces</span>
                            <span class="icon ion-ios-arrow-down"></span>
                        </div>
                        <ul class="links collapse " ${ isNormalMode ? ' id="interfaces-links"' : 'id="xs-interfaces-links"' }>
                            <li class="link">
                                <a href="interfaces/ApiResponse.html" data-type="entity-link" >ApiResponse</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/GatlingAssertionResult.html" data-type="entity-link" >GatlingAssertionResult</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/GatlingTestResult.html" data-type="entity-link" >GatlingTestResult</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/testModel2.html" data-type="entity-link" >testModel2</a>
                            </li>
                            <li class="link">
                                <a href="interfaces/TestResponseModel.html" data-type="entity-link" >TestResponseModel</a>
                            </li>
                        </ul>
                    </li>
                    <li class="chapter">
                        <div class="simple menu-toggler" data-bs-toggle="collapse" ${ isNormalMode ? 'data-bs-target="#miscellaneous-links"'
                            : 'data-bs-target="#xs-miscellaneous-links"' }>
                            <span class="icon ion-ios-cube"></span>
                            <span>Miscellaneous</span>
                            <span class="icon ion-ios-arrow-down"></span>
                        </div>
                        <ul class="links collapse " ${ isNormalMode ? 'id="miscellaneous-links"' : 'id="xs-miscellaneous-links"' }>
                            <li class="link">
                                <a href="miscellaneous/enumerations.html" data-type="entity-link">Enums</a>
                            </li>
                            <li class="link">
                                <a href="miscellaneous/variables.html" data-type="entity-link">Variables</a>
                            </li>
                        </ul>
                    </li>
                        <li class="chapter">
                            <a data-type="chapter-link" href="routes.html"><span class="icon ion-ios-git-branch"></span>Routes</a>
                        </li>
                    <li class="chapter">
                        <a data-type="chapter-link" href="coverage.html"><span class="icon ion-ios-stats"></span>Documentation coverage</a>
                    </li>
                    <li class="divider"></li>
                    <li class="copyright">
                        Documentation generated using <a href="https://compodoc.app/" target="_blank" rel="noopener noreferrer">
                            <img data-src="images/compodoc-vectorise.png" class="img-responsive" data-type="compodoc-logo">
                        </a>
                    </li>
            </ul>
        </nav>
        `);
        this.innerHTML = tp.strings;
    }
});