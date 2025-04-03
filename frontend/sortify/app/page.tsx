'use client';

import dynamic from "next/dynamic";
const Map = dynamic(() => import("./component/map"), {ssr: false});

export default function Page(){
    return(
    <main>
        <h1>Recycling Map</h1>
        <Map/>

    </main>
    );
}
