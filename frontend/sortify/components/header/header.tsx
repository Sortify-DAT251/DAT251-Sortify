import styles from "./header.module.css";
import Searchbar from "../searchbar/searchbar";
import SignupModal from "@/components/signup/signup";
import DropdownMenu from "@/components/dropDownMeny/dropDownMenu";

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
                        <Searchbar />
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