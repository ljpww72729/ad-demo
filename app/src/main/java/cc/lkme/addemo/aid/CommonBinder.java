package cc.lkme.addemo.aid;

import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public class CommonBinder extends Binder implements CommonIdInter {

    private IBinder iBinder;
    private String manufacturer = Build.MANUFACTURER.toUpperCase();
    private String packageName;
    private String sign;
    private String idName;

    public CommonBinder(IBinder iBinder, String packageName, String sign, String idName) {
        this.iBinder = iBinder;
        this.packageName = packageName;
        this.sign = sign;
        this.idName = idName;
    }

    @Override
    public IBinder asBinder() {
        return iBinder;
    }

    @Override
    public String getId() {
        String result = null;
        Parcel data = null;
        Parcel reply = null;
        String interfaceName = "";
        int code = 0;

        try {
            data = Parcel.obtain();
            reply = Parcel.obtain();
            switch (manufacturer.toUpperCase()) {
                case "HUAWEI":
                    code = 1;
                    interfaceName = "com.uodis.opendevice.aidl.OpenDeviceIdentifierService";
                    break;
                case "ASUS":
                    code = 3;
                    interfaceName = "com.asus.msa.SupplementaryDID.IDidAidlInterface";
                    break;
                case "SAMSUNG":
                    code = 1;
                    interfaceName = "com.samsung.android.deviceidservice.IDeviceIdService";
                    break;
                case "ZTE":
                case "FERRMEOS":
                case "SSUI":
                    code = 3;
                    interfaceName = "com.bun.lib.MsaIdInterface";
                    break;
                case "ONEPLUS":
                case "OPPO":
                    code = 1;
                    interfaceName = "com.heytap.openid.IOpenID";
                    break;
                case "LENOVO":
                case "MOTOLORA":
                    code = 1;
                    interfaceName = "com.zui.deviceidservice.IDeviceidInterface";
                    break;
            }
            data.writeInterfaceToken(interfaceName);
            switch (manufacturer) {
                case "ONEPLUS":
                case "OPPO":
                    data.writeString(packageName);
                    data.writeString(sign);
                    data.writeString(idName);
                    break;
            }
            iBinder.transact(code, data, reply, 0);
            reply.readException();
            result = reply.readString();
        } catch (Throwable ignore) {
        } finally {
            if (data != null) {
                data.recycle();
            }
            if (reply != null) {
                reply.recycle();
            }
        }
        return result;
    }

    @Override
    protected boolean onTransact(int code, Parcel data,
                                 Parcel reply, int flags) throws RemoteException {
        String manufacturer = Build.MANUFACTURER.toUpperCase();
        if (manufacturer.equals("LENOVO") || manufacturer.equals("MOTOLORA")) {
            String str = "com.zui.deviceidservice.IDeviceidInterface";
            switch (code) {
                case 1:
                    data.enforceInterface(str);
                    str = getId();
                    reply.writeNoException();
                    reply.writeString(str);
                    return true;
                case 1598968902:
                    reply.writeString(str);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);

            }
        }
        return super.onTransact(code, data, reply, flags);

    }

    public static CommonIdInter genInterface(IBinder iBinder, String packageName, String sign, String idName) {
        if (iBinder == null) {
            return null;
        }
        String manufacturer = Build.MANUFACTURER.toUpperCase();
        String descriptor = "";
        switch (manufacturer.toUpperCase()) {
            case "HUAWEI":
                descriptor = "com.uodis.opendevice.aidl.OpenDeviceIdentifierService";
                break;
            case "ASUS":
                descriptor = "com.asus.msa.SupplementaryDID.IDidAidlInterface";
                break;
            case "SAMSUNG":
                descriptor = "com.samsung.android.deviceidservice.IDeviceIdService";
                break;
            case "ZTE":
            case "FERRMEOS":
            case "SSUI":
                descriptor = "com.bun.lib.MsaIdInterface";
                break;
            case "ONEPLUS":
            case "OPPO":
                descriptor = "com.heytap.openid.IOpenID";
                break;
            case "LENOVO":
            case "MOTOLORA":
                descriptor = "com.zui.deviceidservice.IDeviceidInterface";
                break;
            default:
                return null;
        }
        IInterface iInterface = iBinder.queryLocalInterface(descriptor);
        if (!(iInterface instanceof CommonIdInter)) {
            return new CommonBinder(iBinder, packageName, sign, idName);
        } else {
            return (CommonIdInter) iInterface;
        }
    }
}