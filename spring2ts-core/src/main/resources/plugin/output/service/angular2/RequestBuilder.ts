import {Observable} from "rxjs/Observable";
import {URLSearchParams, Http, BaseRequestOptions, RequestMethod} from "angular2/http";
declare var APP_URL: string;

interface Appendable {
    append(key: string, value: string)
}

let _append = function(res: Appendable, obj: any): string {
    for(let key of Object.keys(obj)) {
        let value = obj[key];
        if(!value) continue;
        if(obj instanceof Object && !(obj instanceof Array)) {
            if(Object.keys(obj).length == 0) continue;
            res.append(key, String(obj));
        } if(value instanceof Array) {
            var values: string[] = value.map(String);
            for(let element of values) {
                res.append(key, element);
            }
        } else {
            res.append(key, String(obj));
        }
    }
    return res.toString();
};

class PathVariable {
    name: string;
    value: string;
    matrixVariables: Object = {};

    constructor(name:string, value?:string) {
        this.name = name;
        this.value = value;
    }

    public build(): string {
        let res: string = this.value;
        if(Object.keys(this.matrixVariables).length > 0) {
            let param: string = _append(new URLSearchParams(), this.matrixVariables);
            res += ';' + param.split('&').join(';');
        }
        return res;
    }

    get placeholder(): string {
        return '${' + this.name + '}'
    }
}

type PathVariables = { [key:string]:PathVariable }

export class HttpMethod {
    constructor(public name:string, public ngMethod: RequestMethod, public payload:boolean) {
    }
}

export class ContentType {
    constructor(public name:string, public map: (value: any) => string) {
    }
}

export let HttpMethods = {
    GET: new HttpMethod("GET", RequestMethod.Get, false),
    HEAD: new HttpMethod("HEAD", RequestMethod.Head, false),
    POST: new HttpMethod("POST", RequestMethod.Post, true),
    PUT: new HttpMethod("PUT", RequestMethod.Put, true),
    PATCH: new HttpMethod("PATCH", RequestMethod.Patch, true),
    DELETE: new HttpMethod("DELETE", RequestMethod.Delete, false),
    OPTIONS: new HttpMethod("OPTIONS", RequestMethod.Options, false)
};

export let ContentTypes = {
    Json: new ContentType("application/json", v => JSON.stringify(v)),
    Form: new ContentType("application/x-www-form-urlencoded", v => _append(new URLSearchParams(), v))
};

export class RequestBuilder {

    public _basePath: string = APP_URL;

    private _method: HttpMethod = HttpMethods.GET;

    private _contentType: ContentType = ContentTypes.Form;

    private _params: Object = {};

    private _pathVariables: PathVariables = {};

    private _matrixVariables: Object = {};

    private _body: Object;

    private _headers: Object = {};

    private _path: string;

    public constructor(method?: HttpMethod, baseURL?:string) {
        if(typeof method !== 'undefined') {
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
        Object.assign(this._params, values);
        return this;
    }

    public queryParam(name: string, value: any): RequestBuilder {
        let obj = {};
        obj[name] = value;
        Object.assign(this._params, obj);
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
            let val = value[name];
            this.pathVariable(name, val);
        }
        return this;
    }

    public matrixVariable(name: string, value: any): RequestBuilder {
        let obj = {};
        obj[name] = value;
        Object.assign(this._matrixVariables, value);
        return this;
    }

    public matrixVariables(value: any): RequestBuilder {
        Object.assign(this._matrixVariables, value);
        return this;
    }

    public pathMatrixVariables(pathVar: string, value: any): RequestBuilder {
        let pv: PathVariable = this._pathVariables[pathVar];
        if(!pv) pv = new PathVariable(pathVar);
        Object.assign(pv.matrixVariables, value);
        this._pathVariables[pathVar] = pv;
        return this;
    }

    public pathMatrixVariable(pathVar: string, name: string, value: any): RequestBuilder {
        let obj = {};
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
        Object.assign(this._body, value);
        return this;
    }

    public bodyPart(name: string, value: any): RequestBuilder {
        let obj = {};
        obj[name] = value;
        return this.bodyParts(obj);
    }

    public header(name: string, value: any): RequestBuilder {
        let obj = {};
        obj[name] = value;
        Object.assign(this._headers, obj);
        return this;
    }

    public headers(value: any): RequestBuilder {
        Object.assign(this._headers, value);
        return this;
    }

    public contentType(value: ContentType): RequestBuilder {
        this._contentType = value;
        this.header('Content-Type', value.name);
        return this;
    }

    public build<T>(http: Http): Observable<T> {
        let options: BaseRequestOptions = new BaseRequestOptions();
        options.url = this.buildUrl();
        options.method = this._method.ngMethod;
        _append(options.headers, this._headers);
        if(this._body) {
            options.body = this.buildBody();
        }
        return http.request(options.url, options)
            .map(res => res.json());
    }

    private buildBody(): string {
        return this._contentType.map(this._body);
    }

    private buildUrl(): string {
        let res: string = this.buildPathVariables(this._path);
        res = this.buildMatrixVariables(res);
        if(Object.keys(this._params).length > 0) {
            res += '?' + _append(new URLSearchParams(), this._params);
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

    private buildPathVariable(pv: PathVariable, path: string) {
        let val: string = pv.build();
        return path.replace(pv.placeholder, val);
    }

    private buildMatrixVariables(path: string) {
        let res: string = path;
        if(Object.keys(this._matrixVariables).length > 0) {
            let param: string = _append(new URLSearchParams(), this._matrixVariables);
            res += ';' + param.split('&').join(';');
        }
        return res;
    }
}