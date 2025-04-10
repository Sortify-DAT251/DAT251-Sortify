'use client';

import dynamic from "next/dynamic";
import styles from './styling/mainContent.module.css';
const Map = dynamic(() => import("./component/map"), {ssr: false});

export default function MainContent(){
    return(
    <main className={styles.mainContent}>
        <h1 className={styles.title}>Recycling map</h1>
        <Map/>
    </main>
    );
}
