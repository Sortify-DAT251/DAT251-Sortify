'use client'
import React, { useState, useEffect } from 'react';
import { Modal, Box, Typography, Button, Grid, TextField } from '@mui/material';
import styles from "../component/header.module.css";

const commonTextFieldSx = {
    width: 250,
    '& .MuiFilledInput-root': {
        height: 35,
    },
    '& .MuiFilledInput-underline:before, .MuiFilledInput-underline:after': {
        borderBottom: 'none',
    },
};

const commonInputStyle = {
    backgroundColor: '#ffffff',
    border: '1px solid #0B540D',
    borderRadius: '4px',
};

const modalStyle = {
    position: 'absolute' as const,
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 500,
    bgcolor: '#87C75C',
    border: '2px solid #0B540D',
    borderRadius: '8px',
    boxShadow: 24,
    p: 4,
    display: 'flex',
    flexDirection: 'column' as const,
    alignItems: 'center',
};

interface ProfileFieldProps {
    label: string;
    placeholder?: string;
    value: string;
    onChange: (newValue: string) => void;
    type?: string;
}

function ProfileField({ label, placeholder = "Endre her", value, onChange, type = "text" }: ProfileFieldProps) {
    return (
        <Grid item>
            <Typography sx={{ fontWeight: 600, marginBottom: 0.5 }}>{label} -</Typography>
            <TextField
                variant="filled"
                fullWidth
                type={type}
                placeholder={placeholder}
                value={value}
                onChange={(e) => onChange(e.target.value)}
                sx={commonTextFieldSx}
                InputProps={{ style: commonInputStyle }}
            />
        </Grid>
    );
}

export default function ProfileForm() {
    // Dummy userId - bytt ut med reell id når autentisering er implementert
    const userId = "a62da4bb-94d3-4516-93f1-777edcc75bfe"
    const API_BASE_URL = "http://localhost:9876/api"

    // Samlet state for profildata (dummy-data)
    const [profileData, setProfileData] = useState({
        username: '',
        email: '',
        firstName: '',
        lastName: '',
        password: ''
    });

    // Samlet state for redigeringsdata, vi starter med tomme strenger
    const [editedData, setEditedData] = useState({
        username: '',
        email: '',
        firstName: '',
        lastName: '',
        password: ''
    });

    const [isLoading, setIsLoading] = useState(true);
    const isLoggedIn = true;
    const [open, setOpen] = useState(false);
    const [isEditing, setIsEditing] = useState(false);

    // Henter dummy-bruker fra backend på mount
    useEffect(() => {
        fetch(`${API_BASE_URL}/users/${userId}`).then((res) => {
            if (!res.ok) {
                throw new Error("Failed to fetch user data");
            }
            return res.json();
        }).then((data) => {
            setProfileData(data);
            setIsLoading(false);
        }).catch((error) => {
            console.error(error);
            setIsLoading(false);
        });
    }, [userId]);

    const handleOpen = () => {
        setOpen(true);
        setIsEditing(false);
    };

    const handleClose = () => {
        setOpen(false);
        setIsEditing(false);
        setEditedData({
            username: '',
            email: '',
            firstName: '',
            lastName: '',
            password: ''
        });
    };

    const handleEdit = () => {
        setIsEditing(true);
        setEditedData({
            username: '',
            email: '',
            firstName: '',
            lastName: '',
            password: ''
        });
    };

    const handleSave = () => {
        const updatedProfile = {
            username: editedData.username.trim() !== '' ? editedData.username : profileData.username,
            email: editedData.email.trim() !== '' ? editedData.email : profileData.email,
            firstName: editedData.firstName.trim() !== '' ? editedData.firstName : profileData.firstName,
            lastName: editedData.lastName.trim() !== '' ? editedData.lastName : profileData.lastName,
            password: editedData.password.trim() !== '' ? editedData.password : profileData.password,
        };
        setProfileData(updatedProfile);
        console.log("Endringer lagret:", updatedProfile);
        handleClose();
    };

    if (!isLoggedIn) {
        return <Typography>You are not logged in.</Typography>;
    }

    if (isLoading) {
        return <Typography>Loading...</Typography>;
    }

    return (
        <div>
            <button className={styles.button} onClick={handleOpen}>
                Profile
            </button>

            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby="profile-modal-title"
                aria-describedby="profile-modal-description"
            >
                <Box sx={modalStyle}>
                    <Typography
                        id="profile-modal-title"
                        variant="h6"
                        sx={{ fontWeight: 600, marginBottom: 2, textAlign: 'center' }}
                    >
                        Your Profile
                    </Typography>

                    {!isEditing ? (
                        // VISNINGSMODUS
                        <Box sx={{ width: '100%', textAlign: 'left' }}>
                            <Typography sx={{ marginBottom: 1 }}>
                                <strong>Username:</strong> {profileData.username}
                            </Typography>
                            <Typography sx={{ marginBottom: 1 }}>
                                <strong>E-mail:</strong> {profileData.email}
                            </Typography>
                            <Typography sx={{ marginBottom: 1 }}>
                                <strong>First name:</strong> {profileData.firstName || '-'}
                            </Typography>
                            <Typography sx={{ marginBottom: 1 }}>
                                <strong>Sure name:</strong> {profileData.lastName || '-'}
                            </Typography>
                            <Button
                                variant="contained"
                                sx={{
                                    backgroundColor: '#ffffff',
                                    color: '#0B540D',
                                    border: '2px solid #0B540D',
                                    fontWeight: 600,
                                    '&:hover': {
                                        backgroundColor: '#6CA047',
                                        borderColor: '#0B540D',
                                        boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
                                    },
                                }}
                                onClick={handleEdit}
                            >
                                Edit profile
                            </Button>
                        </Box>
                    ) : (
                        // REDIGERINGSMODUS: Bruker den gjenbrukbare ProfileField-komponenten
                        <Grid
                            container
                            spacing={2}
                            direction="column"
                            sx={{ width: '90%', maxWidth: 400, margin: '0 auto' }}
                        >
                            <ProfileField
                                label="Username"
                                placeholder="Change here"
                                value={editedData.username}
                                onChange={(newVal) =>
                                    setEditedData((prev) => ({ ...prev, username: newVal }))
                                }
                            />
                            <ProfileField
                                label="E-mail"
                                placeholder="Change here"
                                value={editedData.email}
                                onChange={(newVal) =>
                                    setEditedData((prev) => ({ ...prev, email: newVal }))
                                }
                            />
                            <ProfileField
                                label="First name"
                                placeholder="Change here"
                                value={editedData.firstName}
                                onChange={(newVal) =>
                                    setEditedData((prev) => ({ ...prev, firstName: newVal }))
                                }
                            />
                            <ProfileField
                                label="Sure name"
                                placeholder="Change here"
                                value={editedData.lastName}
                                onChange={(newVal) =>
                                    setEditedData((prev) => ({ ...prev, lastName: newVal }))
                                }
                            />
                            <ProfileField
                                label="Password"
                                type="password"
                                placeholder="Change here"
                                value={editedData.password}
                                onChange={(newVal) =>
                                    setEditedData((prev) => ({ ...prev, password: newVal }))
                                }
                            />

                            <Grid item sx={{ textAlign: 'center' }}>
                                <Button
                                    variant="contained"
                                    sx={{
                                        marginTop: 1,
                                        backgroundColor: '#ffffff',
                                        color: '#0B540D',
                                        border: '2px solid #0B540D',
                                        fontWeight: 600,
                                        '&:hover': {
                                            backgroundColor: '#6CA047',
                                            borderColor: '#0B540D',
                                            boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
                                        },
                                    }}
                                    onClick={handleSave}
                                >
                                    Save changes
                                </Button>
                            </Grid>
                        </Grid>
                    )}
                </Box>
            </Modal>
        </div>
    );
}
