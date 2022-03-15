using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;
using UnityEngine.Networking;
using System;

public class AuthButton : MonoBehaviour
{
    Button button;
    public GameObject Login;
    public GameObject Password;

    string url = "http://engynear.com:22865/authorized";

    void Start()
    {
        button = GetComponent<Button>();
        button.onClick.AddListener(TaskOnClick);
    }

    void TaskOnClick()
    {
        InputField loginInput = Login.GetComponent<InputField>();
        InputField passwordInput = Password.GetComponent<InputField>();
        string loginText = loginInput.text;
        string passwordText = passwordInput.text;
        Debug.Log(loginText);
        Debug.Log(passwordText);
        try
        {
            StartCoroutine(SendRequest(loginText, passwordText));
        }catch(Exception e)
        {
            Debug.Log(e);
        }

        SceneManager.LoadScene("SampleScene");
    }


    private IEnumerator SendRequest(string username, string password)
    {
        UnityWebRequest request = UnityWebRequest.Get(this.url);

        string authorization = authenticate(username, password);
        request.SetRequestHeader("AUTHORIZATION", authorization);
        yield return request.SendWebRequest();
        string answer = request.downloadHandler.text;
        Debug.Log(answer);
    }

    string authenticate(string username, string password)
    {
        string auth = username + ":" + password;
        auth = System.Convert.ToBase64String(System.Text.Encoding.GetEncoding("ISO-8859-1").GetBytes(auth));
        auth = "Basic " + auth;
        return auth;
    }
}

[System.Serializable]
public class requestInfo
{

}
