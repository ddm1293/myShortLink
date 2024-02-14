export const modules = import.meta.glob('./modules/*.js', { eager: true })

for (const module in modules) {
    console.log("see module", module)
}