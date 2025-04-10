import styles from "../styling/header.module.css";
import Searcbar from "./searchbar";
import SignupModal from "@/app/component/signup";
import DropdownMenu from "@/app/component/dropDownMenu";

export default function Header() {
    return (
        <div className={styles.headerContainer}>
            <header className={styles.header}>
                <nav className={styles.nav}>
                    {/* Logo */}
                    <div className={styles.logoWrapper}>
                        <a href="/en/" className={styles.logo}>
                            <img
                                src="/sortify-logo-header.png"
                                alt="sortify logo header version"
                                className={styles.logoImageHeader}
                            />
                        </a>
                    </div>
                    <div className={styles.searchWrapper}>
                        <Searcbar />
                    </div>

                    {/* Navigation Links */}
                    <div className={styles.items}>
                        <SignupModal />
                        <DropdownMenu />
                    </div>
                </nav>
            </header>
        </div>
    );
}
