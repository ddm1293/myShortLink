export const modules = import.meta.glob('./modules/*.js', { eager: true })

const apis = {}
for (const module in modules) {
    const name = module.replace(/^\.\/modules\/(.*)\.\w+$/, '$1')
    const value = modules[module]
    apis[name] = value.default
}

const API = apis

export default API;