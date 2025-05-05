'use client';

import dynamic from "next/dynamic";
import styles from './mainContent.module.css';
const Map = dynamic(() => import('../components/map'), {ssr: false});
import { useSearch } from "@/app/context/searchContext";

export default function Page(){
    return(
        <main className={styles.mainContent}>
            <h1 className={styles.title}>Gjenvinningskart</h1>
            <Map/>
        </main>
    );
}