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
                    <a href="/en/" className={styles.logo}>
                        <img
                            src="/sortify-header.png"
                            alt="sortify logo header version"
                            width="200"
                            className={styles.logoImage}
                        />
                    </a>
                    <Searcbar/>
                    {/* Navigation Links */}
                    <div className={styles.items}>
                        <span >
                            <SignupModal/>
                        </span>

                        <DropdownMenu/>
                    </div>
                </nav>
            </header>
        </div>
    );
}
