import {useEffect} from 'react';
import {useForm} from 'react-hook-form';
import {yupResolver} from '@hookform/resolvers/yup';
import * as yup from 'yup';
import {useNavigate, useParams} from 'react-router-dom';
import axiosSetup from '../api/axiosSetup.ts';
import {toast} from 'react-toastify';

const schema = yup.object({
    login: yup.string().required('Login wymagany'),
    firstName: yup.string().required('Imię wymagane'),
    lastName: yup.string().required('Nazwisko wymagane'),
    email: yup.string().email('Błędny email').required('Email wymagany'),
    password: yup.string().test('req', 'Hasło wymagane', function (val) {
        if (!this.options.context?.isEdit) return !!val && val.length >= 5;
        return true;
    }),
});

export default function UserForm() {
    const {id} = useParams();
    const isEdit = !!id;
    const navigate = useNavigate();

    const {register, handleSubmit, setValue, formState: {errors}} = useForm<any>({
        resolver: yupResolver(schema),
        context: {isEdit}
    });
    useEffect(() => {
        if (isEdit) {
            axiosSetup.get(`/users/${id}`).then(res => {
                const u = res.data;
                setValue('login', u.login);
                setValue('firstName', u.firstName);
                setValue('lastName', u.lastName);
                setValue('email', u.email);
            });
        }
    }, [id]);

    const onSubmit = async (data: any) => {
        if (!window.confirm("Zapisać zmiany?")) return;

        const payload = {
            ...data,
            isActive: true
        };

        try {
            if (isEdit) {
                await axiosSetup.put(`/users/${id}`, payload);
            } else {
                await axiosSetup.post('/users/client', payload);
            }
            toast.success("Dodano nowego użytkownika");
            navigate('/users');
        } catch (e: any) {
            const msg = e.response?.data?.message || "Błąd podczas tworzenia nowego użytkownika, sprawdź wpisane dane";
            toast.error(msg);
        }
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <h2>{isEdit ? 'Edytuj dane uźytkownika' : 'Dodaj nowego uźytkownika'}</h2>

            <label>Login</label>
            <input {...register('login')} disabled={isEdit}/>
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

            {!isEdit && (
                <>
                    <label>Hasło</label>
                    <input type="password" {...register('password')} />
                    <span className="error" style={{color: 'red'}}>{errors.password?.message as string}</span>
                </>
            )}

            <button type="submit" className="btn">Dodaj użytkownika</button>
        </form>
    );
}