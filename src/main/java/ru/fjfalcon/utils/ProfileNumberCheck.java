package ru.fjfalcon.utils;

import java.util.regex.Pattern;

/**
 * Created by fjfalcon on 20.10.16.
 */
public class ProfileNumberCheck
{

    private static final Pattern taxNumberPattern = Pattern.compile("\\d{10}|\\d{12}");
    private static final Pattern insurancePattern = Pattern.compile("\\d{11}");
    private static final Pattern bicPattern = Pattern.compile("\\d{9}");
    private static final Pattern accountPattern = Pattern.compile("\\d{20}");

    private static final int[] taxNumberArr = new int[]{3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
    private static final int[] insuranceArr = new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1};
    private static final int[] accountArr = new int[]{7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1, 3, 7, 1};

    public static boolean isValidTaxNumber(String taxNumber)
    {
        taxNumber = taxNumber.trim();
        if (!taxNumberPattern.matcher(taxNumber).matches())
        {
            return false;
        }
        int length = taxNumber.length();
        if (length == 12)
        {
            return taxNumberStep(taxNumber, 2, 1) && taxNumberStep(taxNumber, 1, 0);
        }
        else
        {
            return taxNumberStep(taxNumber, 1, 2);
        }
    }

    public static boolean isValidInsuranceNumber(String insuranceNumber)
    {
        insuranceNumber = insuranceNumber.trim();
        insuranceNumber = insuranceNumber.replaceAll("\\D+", "");

        return insurancePattern.matcher(insuranceNumber).matches() && insuranceNumber(insuranceNumber);

    }

    public static boolean isValidAccountNumber(String accountNumber, String bic)
    {
        accountNumber = accountNumber.trim();
        accountNumber = accountNumber.replaceAll("\\D+", "");

        bic = bic.trim();
        bic = bic.replaceAll("\\D+", "");

        return bicPattern.matcher(bic).matches() && accountPattern.matcher(accountNumber).matches() && accountNumber(accountNumber, bic);

    }

    public static boolean isValidCardNumber(String ccNumber)
    {
        ccNumber = ccNumber.trim();
        ccNumber = ccNumber.replaceAll("\\D+", "");

        int sum = 0;
        boolean alternate = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--)
        {
            int n = Integer.parseInt(ccNumber.substring(i, i + 1));
            if (alternate)
            {
                n *= 2;
                if (n > 9)
                {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    private static boolean insuranceNumber(String insuranceNumber)
    {
        int sum = 0;

        for (int i = 0; i < insuranceNumber.length() - 2; i++)
        {
            sum += (insuranceNumber.charAt(i) - '0') * insuranceArr[i];
        }

        int controlNumber = sum % 101;

        return controlNumber == Integer.parseInt(insuranceNumber.substring(insuranceNumber.length() - 2)) || insuranceNumber.substring(insuranceNumber.length() - 2).equals("00") && (controlNumber == 100 || controlNumber == 101);

    }

    private static boolean accountNumber(String accountNumber, String bic)
    {
        int sum = 0;

        String bicCheck = bic.substring(bic.length() - 3);

        String checkNumber = bicCheck + accountNumber;

        for (int i = 0; i < checkNumber.length(); i++)
        {
            sum += (checkNumber.charAt(i) - '0') * accountArr[i];
        }

        return sum % 10 == 0;
    }


    private static boolean taxNumberStep(String taxNumber, int offset, int arrOffset)
    {
        int sum = 0;
        int length = taxNumber.length();
        for (int i = 0; i < length - offset; i++)
        {
            sum += (taxNumber.charAt(i) - '0') * taxNumberArr[i + arrOffset];
        }
        return (sum % 11) % 10 == taxNumber.charAt(length - offset) - '0';
    }

}
