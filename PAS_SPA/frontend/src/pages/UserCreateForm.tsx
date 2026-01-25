import {useForm} from 'react-hook-form';
import {yupResolver} from '@hookform/resolvers/yup';
import * as yup from 'yup';
import {useNavigate} from 'react-router-dom';
import axiosSetup from '../api/axiosSetup.ts';
import {toast} from 'react-toastify';

const schema = yup.object({
    login: yup.string().required('Login wymagany'),
    firstName: yup.string().required('Imię wymagane'),
    lastName: yup.string().required('Nazwisko wymagane'),
    email: yup.string().email('Błędny email').required('Email wymagany'),
    password: yup.string().required('Hasło wymagane').min(8, 'Minimum 8 znaków'),
});

export default function UserCreateForm() {
    const navigate = useNavigate();

    const {register, handleSubmit, formState: {errors}} = useForm({
        resolver: yupResolver(schema)
    });

    const onSubmit = async (data: any) => {
        if (!window.confirm("Zapisać zmiany?")) return;

        const payload = {
            ...data,
            isActive: true
        };

        try {
            await axiosSetup.post('/users/client', payload);
            toast.success("Dodano nowego użytkownika");
            navigate('/users');
        } catch (e: any) {
            const msg = e.response?.data?.message || "Błąd podczas tworzenia użytkownika";
            toast.error(msg);
        }
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <h2>Dodaj nowego użytkownika</h2>

            <label>Login</label>
            <input {...register('login')} />
            <span className="error" style={{color: 'red'}}>{errors.login?.message as string}</span>

            <label>Imię</label>
            <input {...register('firstName')} />
            <span className="error" style={{color: 'red'}}>{errors.firstName?.message as string}</span>

            <label>Nazwisko</label>
            <input {...register('lastName')} />
            <span className="error" style={{color: 'red'}}>{errors.lastName?.message as string}</span>

            <label>Email</label>
            <input {...register('email')} />
            <span className="error" style={{color: 'red'}}>{errors.email?.message as string}</span>

            <label>Hasło</label>
            <input type="password" {...register('password')} />
            <span className="error" style={{color: 'red'}}>{errors.password?.message as string}</span>

            <button type="submit" className="btn">Dodaj użytkownika</button>
        </form>
    );
}