import styles from "./header.module.css";
import Searchbar from "../searchbar";
import SignupModal from "@/app/component/signup/signup";
import DropdownMenu from "@/app/component/dropDownMeny/dropDownMenu";

export default function Header() {
    return (
        <div className={styles.headerContainer}>
            <header className={styles.header}>
                <nav className={styles.nav}>
                    {/* Logo */}
                    <div className={styles.nav}>
                        <a href="/frontend/sortify/public" className={styles.logo}>
                            <img
                                src="/sortify-logo-header.png"
                                alt="sortify logo header version"
                                style={{height: "75px", width: "75px"}}
                                className={styles.logoImageHeader}
                            />
                        </a>
                    </div >
                    <div className={styles.nav}>
                        <div className={styles.searchbar}>
                            <Searchbar/>
                        </div>
                    </div>

                    {/* Navigation Links */}
                    <div className={styles.nav}>
                        <div className={styles.items}>
                        <span>
                            <SignupModal/>
                        </span>
                            <DropdownMenu/>
                        </div>
                    </div>

                </nav>
            </header>
        </div>
    );
}
