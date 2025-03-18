import type { Options as VueOptions } from '@vitejs/plugin-vue';
import type { Options as VueJsxOptions } from '@vitejs/plugin-vue-jsx';
import type { AstroIntegration, ContainerRenderer } from 'astro';
import type { VitePluginVueDevToolsOptions } from 'vite-plugin-vue-devtools';
interface Options extends VueOptions {
    jsx?: boolean | VueJsxOptions;
    appEntrypoint?: string;
    devtools?: boolean | Omit<VitePluginVueDevToolsOptions, 'appendTo'>;
}
export declare function getContainerRenderer(): ContainerRenderer;
export default function (options?: Options): AstroIntegration;
export {};
