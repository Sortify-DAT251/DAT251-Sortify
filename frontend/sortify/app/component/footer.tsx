import styles from "../component/footer.module.css";
import React from 'react';

export default function Footer() {
    return (
        <div className={styles.footerContainer}>
            <div className={styles.footer}>
                <p className={styles.footerText}>
                    &copy; 2025 Sortify. All rights reserved.
                </p>
            </div>
        </div>
    );
}
