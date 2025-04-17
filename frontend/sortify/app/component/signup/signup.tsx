'use client';

import React, { useState } from 'react';
import styles from './signup.module.css'; // your existing styles

export default function SignupModal() {
    const [open, setOpen] = useState(false);
    const [isSignup, setIsSignup] = useState(false);
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);
    const handleToggleForm = () => setIsSignup(prev => !prev);

    return (
        <>
            <button onClick={handleOpen} className={styles.buttonPrimary}>
                Sign Up / In
            </button>

            {open && (
                <div className={styles.modalOverlay}>
                    <div className={styles.modalContainer}>
                        <h2 className={styles.modalTitle}>
                            {isSignup ? 'Create an account' : 'Log in to your account'}
                        </h2>

                        <form
                            className={styles.form}
                            onSubmit={(e) => {
                                e.preventDefault();
                                // handle submit logic here
                            }}
                        >
                            {isSignup && (
                                <input
                                    type="text"
                                    placeholder="Username"
                                    value={username}
                                    onChange={(e) => setUsername(e.target.value)}
                                    className={styles.inputField}
                                    required
                                />
                            )}

                            <input
                                type="email"
                                placeholder="Email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                className={styles.inputField}
                                required
                            />

                            <input
                                type="password"
                                placeholder="Password"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                                className={styles.inputField}
                                required
                            />

                            <button type="submit" className={styles.buttonPrimary}>
                                {isSignup ? 'Sign up' : 'Sign in'}
                            </button>
                        </form>

                        <button
                            className={styles.buttonSecondary}
                            onClick={handleToggleForm}
                        >
                            {isSignup
                                ? 'Already have an account?'
                                : "Don't have an account?"}
                        </button>

                        <button
                            className={styles.modalCloseButton}
                            onClick={handleClose}
                            aria-label="Close modal"
                        >
                            ×
                        </button>
                    </div>
                </div>
            )}
        </>
    );
}
