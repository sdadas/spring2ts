import * as $ from "jquery";
declare var APP_URL: string;

class PathVariable {
    public name: string;
    public value: string;
    public matrixVariables: Object = {};

    constructor(name:string, value?:string) {
        this.name = name;
        this.value = value;
    }

    public build(): string {
        let res: string = this.value;
        if(Object.keys(this.matrixVariables).length > 0) {
            let param: string = $.param(this.matrixVariables);
            res += ";" + param.split("&").join(";");
        }
        return res;
    }

    get placeholder(): string {
        return "${" + this.name + "}";
    }
}

type PathVariables = { [key:string]:PathVariable }

export class HttpMethod {
    constructor(public name:string, public payload:boolean) {
    }
}

export class ContentType {
    constructor(public name:string, public map: (value: any) => string) {
    }
}

export let HttpMethods: any = {
    GET: new HttpMethod("GET", false),
    HEAD: new HttpMethod("HEAD", false),
    POST: new HttpMethod("POST", true),
    PUT: new HttpMethod("PUT", true),
    PATCH: new HttpMethod("PATCH", true),
    DELETE: new HttpMethod("DELETE", false),
    OPTIONS: new HttpMethod("OPTIONS", false),
};

export let ContentTypes: any = {
    Json: new ContentType("application/json", v => JSON.stringify(v)),
    Form: new ContentType("application/x-www-form-urlencoded", v => $.param(v)),
};

export class RequestBuilder {

    public _basePath: string = APP_URL;

    private _method: HttpMethod = HttpMethods.GET;

    private _contentType: ContentType = ContentTypes.Json;

    private _params: Object = {};

    private _pathVariables: PathVariables = {};

    private _matrixVariables: Object = {};

    private _body: Object;

    private _headers: Object = {};

    private _path: string;

    public constructor(method?: HttpMethod, baseURL?:string) {
        if(typeof method !== "undefined") {
            this._method = method;
        }
        if(typeof baseURL !== "undefined") {
            this._basePath = baseURL;
        }
    }

    public method(value: HttpMethod): RequestBuilder {
        this._method = value;
        return this;
    }

    public path(value: string): RequestBuilder {
        this._path = this._basePath + value;
        return this;
    }

    public queryParams(values: Object): RequestBuilder {
        $.extend(this._params, values);
        return this;
    }

    public queryParam(name: string, value: any): RequestBuilder {
        let obj: any = {};
        obj[name] = value;
        $.extend(this._params, obj);
        return this;
    }

    public pathVariable(name: string, value: any): RequestBuilder {
        let pv: PathVariable = this._pathVariables[name];
        if(!pv) pv = new PathVariable(name);
        pv.value = String(value);
        this._pathVariables[name] = pv;
        return this;
    }

    public pathVariables(value: {[key:string]:string}): RequestBuilder  {
        for(let name of Object.keys(value)) {
            let val:any = value[name];
            this.pathVariable(name, val);
        }
        return this;
    }

    public matrixVariable(name: string, value: any): RequestBuilder {
        let obj: any = {};
        obj[name] = value;
        $.extend(this._matrixVariables, value);
        return this;
    }

    public matrixVariables(value: any): RequestBuilder {
        $.extend(this._matrixVariables, value);
        return this;
    }

    public pathMatrixVariables(pathVar: string, value: any): RequestBuilder {
        let pv: PathVariable = this._pathVariables[pathVar];
        if(!pv) pv = new PathVariable(pathVar);
        $.extend(pv.matrixVariables, value);
        this._pathVariables[pathVar] = pv;
        return this;
    }

    public pathMatrixVariable(pathVar: string, name: string, value: any): RequestBuilder {
        let obj: any = {};
        obj[name] = value;
        return this.pathMatrixVariables(pathVar, obj);
    }

    public body(value: Object): RequestBuilder {
        this._body = value;
        return this;
    }

    public bodyParts(value: Object): RequestBuilder {
        if(!this._body) {
            this._body = {};
        }
        $.extend(this._body, value);
        return this;
    }

    public bodyPart(name: string, value: any): RequestBuilder {
        let obj: any = {};
        obj[name] = value;
        return this.bodyParts(obj);
    }

    public header(name: string, value: any): RequestBuilder {
        let obj: any = {};
        obj[name] = value;
        $.extend(this._headers, obj);
        return this;
    }

    public headers(value: any): RequestBuilder {
        $.extend(this._headers, value);
        return this;
    }

    public contentType(value: ContentType): RequestBuilder {
        this._contentType = value;
        this.header("Content-Type", value.name);
        return this;
    }

    public build<T>(): JQueryPromise<T> {
        let settings: JQueryAjaxSettings = {
            url: this.buildUrl(),
            headers: this._headers,
            type: this._method.name,
            processData: false,
            crossDomain: true,
            xhrFields: {
                withCredentials: true,
            },
        } as JQueryAjaxSettings;
        if(this._body) {
            settings.data = this.buildBody();
        }
        return <JQueryPromise<T>> $.ajax(settings);
    }

    private buildBody(): string {
        return this._contentType.map(this._body);
    }

    private buildUrl(): string {
        let res: string = this.buildPathVariables(this._path);
        res = this.buildMatrixVariables(res);
        if(Object.keys(this._params).length > 0) {
            res += "?" + $.param(this._params);
        }
        return res;
    }

    private buildPathVariables(path: string): string {
        let res: string = path;
        for(let key of Object.keys(this._pathVariables)) {
            let pv: PathVariable = this._pathVariables[key];
            res = this.buildPathVariable(pv, res);
        }
        return res;
    }

    private buildPathVariable(pv: PathVariable, path: string):string {
        let val: string = pv.build();
        return path.replace(pv.placeholder, val);
    }

    private buildMatrixVariables(path: string):string {
        let res: string = path;
        if(Object.keys(this._matrixVariables).length > 0) {
            let param: string = $.param(this._matrixVariables);
            res += ";" + param.split("&").join(";");
        }
        return res;
    }
}
