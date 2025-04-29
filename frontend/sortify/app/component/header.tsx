import styles from "../component/header.module.css";
import Searcbar from "./searchbar";
import SignupModal from "@/app/component/signup";
import DropdownMenu from "@/app/component/dropDownMenu";
import ProfileForm from "@/app/component/profile";

export default function Header() {
    return (
        <div className={styles.headerContainer}>
            <header className={styles.header}>
                <nav className={styles.nav}>
                    {/* Logo */}
                    <div className={styles.nav}>
                        <a href="/en/" className={styles.logo}>
                            <img
                                src="/sortify-logo-header.png"
                                alt="sortify logo header version"
                                style={{height: "100px", width: "100px"}}
                                className={styles.logoImageHeader}
                            />
                        </a>
                    </div >
                    <div className={styles.nav}>
                        <div className={styles.searchbar}>
                            <Searcbar/>
                        </div>
                    </div>

                    {/* Navigation Links */}
                    <div className={styles.nav}>
                        <div className={styles.items}>
                        <span>
                            <SignupModal/>
                        </span>
                        <span>
                            <ProfileForm/>
                        </span>

                            <DropdownMenu/>
                        </div>
                    </div>

                </nav>
            </header>
        </div>
    );
}
