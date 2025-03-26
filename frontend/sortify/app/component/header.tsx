import styles from "../component/header.module.css";
import Searcbar from "./searchbar";

export default function Header() {
    return (
        <div className={styles.headerContainer}>
            <header className={styles.header}>
                <nav className={styles.nav}>
                    {/* Logo */}
                    <a href="/en/" className={styles.logo}>
                        <img
                            src="/sortify-header.png"
                            alt="sortify logo"
                            width="200"
                            className={styles.logoImage}
                        />
                    </a>

                    <Searcbar/>

                    {/* Navigation Links */}
                    <div className={styles.links}>
                        <a href="/en/" className={styles.button}>
                            Home
                        </a>
                        <a href="/en/stats" className={styles.button}>
                            Stats
                        </a>
                        <a
                            href="https://docs.astro.build/en/develop-and-build/"
                            className={styles.button}
                        >
                            Documentation
                        </a>
                    </div>
                </nav>
            </header>
        </div>
    );
}
