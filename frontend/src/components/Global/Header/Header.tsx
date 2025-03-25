import { useEffect, useState } from "react";
import styles from "./Header.module.css";

interface HeaderProps {
  lang: "en" | "no";
  translations: {
    home: string;
    stats: string;
    docs: string;
  };
}

export default function Header({ lang, translations }: HeaderProps) {
  const [currentLang, setCurrentLang] = useState(lang);

  useEffect(() => {
    setCurrentLang(lang);
  }, [lang]);

  return (
    <div className={styles.headerContainer}>
      <header className={styles.header}>
        <nav className={styles.nav}>
          {/* Logo */}
          <a href={`/${currentLang}/`} className={styles.logo}>
            <img
              src="/sortify-header.png"
              alt="sortify logo"
              width="200"
              className={styles.logoImage}
            />
          </a>
          {/* Navigation Links */}
          <div className={styles.links}>
            <a href={`/${currentLang}/`} className={styles.button}>
              {translations.home}
            </a>
            <a href={`/${currentLang}/stats`} className={styles.button}>
              {translations.stats}
            </a>
            <a
              href="https://docs.astro.build/en/develop-and-build/"
              className={styles.button}
            >
              {translations.docs}
            </a>
          </div>
        </nav>
      </header>
    </div>
  );
}
