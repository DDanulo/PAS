import {useEffect, useState} from 'react';
import {useForm} from 'react-hook-form';
import {yupResolver} from '@hookform/resolvers/yup';
import * as yup from 'yup';
import {useNavigate, useParams} from 'react-router-dom';
import axiosSetup from '../api/axiosSetup.ts';
import {toast} from 'react-toastify';
import {useAuth} from '../context/LoggedUserContext';
import {RoleEnum} from "../HandleProtection.tsx";

const userSchema = yup.object({
    login: yup.string().required('Login wymagany'),
    firstName: yup.string().required('Imię wymagane'),
    lastName: yup.string().required('Nazwisko wymagane'),
    email: yup.string().email('Błędny email').required('Email wymagany'),
});

const passwordSchema = yup.object({
    oldPassword: yup.string().required('Stare hasło jest wymagane'),
    newPassword: yup.string()
        .min(8, 'Hasło musi mieć min. 8 znaków')
        .required('Nowe hasło jest wymagane'),
    confirmPassword: yup.string()
        .oneOf([yup.ref('newPassword')], 'Hasła muszą być identyczne')
        .required('Potwierdzenie hasła jest wymagane'),
});

export default function UserEditForm() {
    const {id} = useParams();
    const navigate = useNavigate();
    const {userLogin, userRole} = useAuth();

    const [etag, setEtag] = useState<string | null>(null);

    const {
        register: registerData,
        handleSubmit: handleSubmitData,
        setValue: setValueData,
        formState: {errors: errorsData}
    } = useForm({
        resolver: yupResolver(userSchema)
    });

    const {
        register: registerPass,
        handleSubmit: handleSubmitPass,
        reset: resetPass,
        formState: {errors: errorsPass}
    } = useForm({
        resolver: yupResolver(passwordSchema)
    });

    useEffect(() => {
        if (id) {
            axiosSetup.get(`/users/id/${id}`).then(res => {
                const u = res.data;
                setValueData('login', userLogin);
                setValueData('firstName', u.firstName);
                setValueData('lastName', u.lastName);
                setValueData('email', u.email);

                const etagHeader = res.headers['etag'];
                setEtag(etagHeader);
            }).catch(() => {
                toast.error("Nie udało się pobrać danych użytkownika");
                navigate('/users');
            });
        } else {
            axiosSetup.get(`/me`).then(res => {
                const u = res.data;

                setValueData('login', userLogin);
                setValueData('firstName', u.firstName);
                setValueData('lastName', u.lastName);
                setValueData('email', u.email);

                const etagHeader = res.headers['etag'];
                setEtag(etagHeader);
            }).catch(() => {
                toast.error("Nie udało się pobrać danych użytkownika");
                navigate('/me');
            });
        }

    }, [id, setValueData, navigate, userLogin]);

    const onSubmitData = async (data: any) => {
        if (!window.confirm("Zapisać zmiany?")) return;

        const payload = {
            ...data,
            isActive: true
        };

        try {
            if (userRole === RoleEnum.ADMIN) {
                await axiosSetup.put(`/users/${id}`, payload, {
                    headers: {
                        'If-Match': etag
                    }
                });
                toast.success("Zaktualizowano dane użytkownika");
            } else {
                await axiosSetup.put(`/me`, payload, {
                    headers: {
                        'If-Match': etag
                    }
                });
                toast.success("Twoje dane zostały zaktualizowane");
            }
        } catch (e: any) {
            const msg = e.response?.data?.message || "Wystąpił błąd podczas edycji";
            toast.error(msg);
        }
    };

    const onSubmitPassword = async (data: any) => {
            if (!window.confirm("Czy na pewno chcesz zmienić hasło?")) return;

            const payload = {
                oldPassword: data.oldPassword,
                newPassword: data.newPassword
            };

            try {
                if (userRole === RoleEnum.ADMIN) {
                    await axiosSetup.patch(`/users/${id}/password`, payload);
                }else{
                    await axiosSetup.patch(`/me/password`, payload);
                }
                toast.success("Hasło zostało zmienione");
                resetPass();
            } catch
                (e: any) {
                const msg = e.response?.data?.message || "Błąd zmiany hasła (sprawdź stare hasło)";
                toast.error(msg);
            }
        }
    ;

    return (
        <div className="container">
            <form onSubmit={handleSubmitData(onSubmitData)} className="form-section user-data-form">
                <h2>Edytuj dane użytkownika</h2>

                <div className="form-group">
                    <label>Login</label>
                    <input {...registerData('login')} disabled className="form-control"/>
                    <span className="error-message">{errorsData.login?.message as string}</span>
                </div>

                <div className="form-group">
                    <label>Imię</label>
                    <input {...registerData('firstName')} className="form-control"/>
                    <span className="error-message">{errorsData.firstName?.message as string}</span>
                </div>

                <div className="form-group">
                    <label>Nazwisko</label>
                    <input {...registerData('lastName')} className="form-control"/>
                    <span className="error-message">{errorsData.lastName?.message as string}</span>
                </div>

                <div className="form-group">
                    <label>Email</label>
                    <input {...registerData('email')} className="form-control"/>
                    <span className="error-message">{errorsData.email?.message as string}</span>
                </div>

                <button type="submit" className="btn btn-primary">
                    Zapisz dane osobowe
                </button>
            </form>

            <form onSubmit={handleSubmitPass(onSubmitPassword)} className="form-section password-section">
                <h3>Zmień hasło</h3>

                <div className="form-group">
                    <label>Stare hasło</label>
                    <input type="password" {...registerPass('oldPassword')} className="form-control"/>
                    <span className="error-message">{errorsPass.oldPassword?.message as string}</span>
                </div>

                <div className="form-group">
                    <label>Nowe hasło</label>
                    <input type="password" {...registerPass('newPassword')} className="form-control"/>
                    <span className="error-message">{errorsPass.newPassword?.message as string}</span>
                </div>

                <div className="form-group">
                    <label>Potwierdź nowe hasło</label>
                    <input type="password" {...registerPass('confirmPassword')} className="form-control"/>
                    <span className="error-message">{errorsPass.confirmPassword?.message as string}</span>
                </div>

                <button type="submit" className="btn btn-warning">
                    Zmień hasło
                </button>
            </form>
        </div>
    );
}