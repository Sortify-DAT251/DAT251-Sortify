import styles from "../component/header.module.css";
import Searcbar from "./searchbar";
import SignupModal from "@/app/component/signup";
import DropdownMenu from "@/app/component/dropDownMenu"; // Import the DropdownMenu component

export default function Header() {
    return (
        <div className={styles.headerContainer}>
            <header className={styles.header}>
                <nav className={styles.nav}>
                    {/* Logo */}
                    <div>
                        <a href="/en/" className={styles.logo}>
                            <img
                                src="/sortify-logo-header.png"
                                alt="sortify logo header version"
                                style={{height: "100px", width: "100px"}}
                                className={styles.logoImageHeader}
                            />
                        </a>
                    </div>
                    <div className={styles.searchbar}>
                        <Searcbar/>
                    </div>
                    {/* Navigation Links */}
                    <div className={styles.items}>
                        <span>
                            <SignupModal/>
                        </span>

                        <DropdownMenu/>
                    </div>
                </nav>
            </header>
        </div>
    );
}
